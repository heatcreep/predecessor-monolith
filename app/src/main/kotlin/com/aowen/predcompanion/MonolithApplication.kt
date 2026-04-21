package com.aowen.predcompanion

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class MonolithApplication : Application(), Configuration.Provider {

    @Inject lateinit var itemRepository: ItemRepository

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HiltWorkerFactoryEntryPoint {
        fun workerFactory(): HiltWorkerFactory
    }

    override val workManagerConfiguration: Configuration =
        Configuration.Builder()
            .setWorkerFactory(EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).workerFactory())
            .build()

    override fun onCreate() {
        super.onCreate()
        // Warm the item cache at startup so lookups later are instant.
        // Fire-and-forget: downstream calls to fetchAllItems() will wait on the same
        // mutex if they race the warmup.
        appScope.launch { itemRepository.fetchAllItems() }
    }
}
