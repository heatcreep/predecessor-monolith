package com.aowen.predcompanion.core.datastore.di

import android.content.Context
import com.aowen.predcompanion.core.common.di.IoDispatcher
import com.aowen.predcompanion.core.datastore.ClaimedPlayerPreferencesManager
import com.aowen.predcompanion.core.datastore.ClaimedPlayerPreferencesManagerImpl
import com.aowen.predcompanion.core.datastore.ThemePreferences
import com.aowen.predcompanion.core.datastore.ThemePreferencesImpl
import com.aowen.predcompanion.core.datastore.UserPreferencesManager
import com.aowen.predcompanion.core.datastore.UserPreferencesManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @IoDispatcher
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    fun providesClaimedPlayerPreferencesManager(@ApplicationContext appContext: Context): ClaimedPlayerPreferencesManager {
        return ClaimedPlayerPreferencesManagerImpl(appContext)
    }

    @Provides
    @Singleton
    fun providesUserPreferencesManager(@ApplicationContext appContext: Context): UserPreferencesManager {
        return UserPreferencesManagerImpl(appContext)
    }

    @Provides
    @Singleton
    fun providesThemePreferences(@ApplicationContext appContext: Context): ThemePreferences {
        return ThemePreferencesImpl(appContext)
    }
}