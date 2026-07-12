package com.aowen.predcompanion

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.network.CacheStrategy
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.util.DebugLogger
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MonolithApplication : Application(), Configuration.Provider, SingletonImageLoader.Factory{

    @Inject
    lateinit var itemRepository: ItemRepository
    @Inject
    lateinit var heroRepository: HeroRepository

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HiltWorkerFactoryEntryPoint {
        fun workerFactory(): HiltWorkerFactory
    }

    override val workManagerConfiguration: Configuration =
        Configuration.Builder()
            .setWorkerFactory(
                EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).workerFactory()
            )
            .build()

    @OptIn(ExperimentalCoilApi::class)
    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(OkHttpNetworkFetcherFactory(
                    cacheStrategy = { CacheStrategy.DEFAULT }
                ))
            }
            .logger(DebugLogger())
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        // Warm the item cache at startup so lookups later are instant.
        // Fire-and-forget: downstream calls to fetchAllItems() will wait on the same
        // mutex if they race the warmup.
        appScope.launch {
            itemRepository.fetchAllItems()
            heroRepository.fetchAllHeroes()
        }
    }
}
