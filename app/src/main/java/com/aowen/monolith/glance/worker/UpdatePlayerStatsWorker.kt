package com.aowen.monolith.glance.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.aowen.monolith.data.repository.players.di.PlayerRepository
import com.aowen.monolith.glance.PlayerStatsAppWidget
import com.aowen.monolith.glance.state.PlayerStatsState
import com.aowen.monolith.glance.state.PlayerStatsStateDefinition
import com.aowen.monolith.network.ClaimedPlayerPreferencesManagerImpl
import com.aowen.monolith.network.getOrThrow
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit


@HiltWorker
class UpdatePlayerStatsWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val omedaCityPlayerRepository: PlayerRepository
) : CoroutineWorker(context, params) {

    companion object {

        private const val FORCE_KEY = "force"
        private const val REPEAT_INTERVAL_MINUTES = 15L

        const val CHANNEL_ID = "ForegroundServiceChannel"
        const val NOTIFICATION_ID = 1

        private val uniqueWorkName = UpdatePlayerStatsWorker::class.java.simpleName

        fun startPeriodicStatsUpdate(
            context: Context,
            glanceId: GlanceId
        ) {
            val manager = WorkManager.getInstance(context)

            // Create request for periodic work updating player stats every 15 minutes
            val requestBuilder = PeriodicWorkRequest.Builder(
                workerClass = UpdatePlayerStatsWorker::class.java,
                repeatInterval = REPEAT_INTERVAL_MINUTES,
                repeatIntervalTimeUnit = TimeUnit.MINUTES
            ).addTag(glanceId.toString())


            // Enqueue the periodic work request
            manager.enqueueUniquePeriodicWork(
                "${uniqueWorkName}Periodic",
                ExistingPeriodicWorkPolicy.KEEP,
                requestBuilder.build()
            )
        }

        fun enqueue(
            context: Context,
            glanceId: GlanceId,
            force: Boolean = false
        ) {
            val manager = WorkManager.getInstance(context)

            // Create request for one-time work updating player stats
            val requestBuilder = OneTimeWorkRequestBuilder<UpdatePlayerStatsWorker>().apply {
                addTag(glanceId.toString())
                setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                setInputData(
                    Data.Builder().putBoolean(FORCE_KEY, force).build()
                )
            }

            // decide whether to replace or keep existing work
            val workPolicy = if (force) {
                ExistingWorkPolicy.REPLACE
            } else {
                ExistingWorkPolicy.KEEP
            }

            // Enqueue the one-time work request
            manager.enqueueUniqueWork(
                "${uniqueWorkName}OneTime",
                workPolicy,
                requestBuilder.build()
            )
        }

        // Cancel all work for a given glanceId
        fun cancel(context: Context, glanceId: GlanceId) {
            WorkManager.getInstance(context).cancelAllWorkByTag(glanceId.toString())
        }
    }

    override suspend fun doWork(): Result {

        setWidgetState(PlayerStatsState.Loading)
        return try {

            // Get the claimed player id from the preferences data store
            val claimedPlayerPreferencesManager =
                ClaimedPlayerPreferencesManagerImpl(applicationContext)
            val playerId = claimedPlayerPreferencesManager.claimedPlayerId.firstOrNull()

            // if the player id is not null, fetch the player info and update the widget state
            if (playerId != null) {
                val playerInfo = omedaCityPlayerRepository
                    .fetchPlayerInfo(playerId).getOrThrow()
                if (playerInfo.playerDetails != null) {
                    setWidgetState(
                        PlayerStatsState.Success(
                            playerInfo = playerInfo,
                            playerRankUri = ""
                        )
                    )
                } else {
                    // if the player info is null, set the widget state to error
                    setWidgetState(
                        PlayerStatsState.Error(
                            message = "Failed to fetch player details"
                        )
                    )
                }
            } else {
                // if the player id is null, set the widget state to not set
                setWidgetState(PlayerStatsState.NotSet)
            }
            Result.success()
        } catch (exception: Exception) {
            Log.e(uniqueWorkName, "Failed to update player stats", exception)
            if (runAttemptCount < 10) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        createNotificationChannel()
        val notification = createNotification()
        return ForegroundInfo(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Monolith Player Stats")
            .setContentText("Running in the background")
            .build()
    }

    private suspend fun setWidgetState(newState: PlayerStatsState) {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(PlayerStatsAppWidget::class.java)
        glanceIds.forEach { glanceId ->
            updateAppWidgetState(
                context = context,
                definition = PlayerStatsStateDefinition,
                glanceId = glanceId,
                updateState = { newState }
            )
        }
        PlayerStatsAppWidget().updateAll(context)
    }
}