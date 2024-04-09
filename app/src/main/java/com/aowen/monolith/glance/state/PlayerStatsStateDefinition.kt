package com.aowen.monolith.glance.state

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.glance.state.GlanceStateDefinition
import com.aowen.monolith.data.PlayerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@Polymorphic
sealed interface PlayerStatsState {
    @Serializable
    @SerialName("Loading")
    data object Loading : PlayerStatsState

    @Serializable
    @SerialName("NotSet")
    data object NotSet : PlayerStatsState

    @Serializable
    @SerialName("Success")
    data class Success(
        val playerInfo: PlayerInfo,
        val playerRankUri: String
    ) : PlayerStatsState

    @Serializable
    @SerialName("Error")
    data class Error(
        val message: String
    ) : PlayerStatsState
}

object PlayerStatsStateDefinition : GlanceStateDefinition<PlayerStatsState> {
    private val Context.playerStatsWidgetStore by dataStore(
        fileName = "player_stats_widget_state.json",
        serializer = PlayerStatsStateSerializer
    )

    override fun getLocation(context: Context, fileKey: String): File {
        val directory = context.filesDir.resolve("glance")
        return File(directory, "$fileKey.json")
    }

    override suspend fun getDataStore(
        context: Context,
        fileKey: String
    ): DataStore<PlayerStatsState> {
        return context.playerStatsWidgetStore

    }
}

object PlayerStatsStateSerializer : Serializer<PlayerStatsState> {

    private val json = Json {
        serializersModule = SerializersModule {
            polymorphic(PlayerStatsState::class) {
                subclass(PlayerStatsState.Loading::class, PlayerStatsState.Loading.serializer())
                subclass(PlayerStatsState.NotSet::class, PlayerStatsState.NotSet.serializer())
                subclass(PlayerStatsState.Success::class, PlayerStatsState.Success.serializer())
            }
        }
    }

    override val defaultValue: PlayerStatsState = PlayerStatsState.Loading

    override suspend fun readFrom(input: InputStream): PlayerStatsState = try {
        json.decodeFromString(
            serializer(),
            input.readBytes().decodeToString()
        )
    } catch (exception: SerializationException) {
        throw CorruptionException("Cannot read data: ${exception.message}")
    }

    override suspend fun writeTo(t: PlayerStatsState, output: OutputStream) = withContext(Dispatchers.IO) {
        output.use { output ->
            output.write(json.encodeToString(serializer(), t).toByteArray())
        }
    }
}