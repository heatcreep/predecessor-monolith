package com.aowen.predcompanion.core.database.di

import com.aowen.predcompanion.core.database.MonolithDatabase
import com.aowen.predcompanion.core.database.dao.CurrentUserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesCurrentUserDao(
        database: MonolithDatabase
    ): CurrentUserDao = database.currentUserDao()
}
