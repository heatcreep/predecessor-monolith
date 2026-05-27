package com.aowen.predcompanion.core.testing.di

import com.aowen.predcompanion.core.common.network.Dispatcher
import com.aowen.predcompanion.core.common.network.PredCompanionDispatchers
import com.aowen.predcompanion.core.common.network.di.DispatchersModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class],
)
internal object TestDispatchersModule {
    @Provides
    @Dispatcher(PredCompanionDispatchers.IO)
    fun providesIODispatcher(testDispatcher: TestDispatcher): CoroutineDispatcher = testDispatcher

    @Provides
    @Dispatcher(PredCompanionDispatchers.Default)
    fun providesDefaultDispatcher(
        testDispatcher: TestDispatcher,
    ): CoroutineDispatcher = testDispatcher
}