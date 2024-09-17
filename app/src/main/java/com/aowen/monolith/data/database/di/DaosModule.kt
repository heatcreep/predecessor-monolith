package com.aowen.monolith.data.database.di

import com.aowen.monolith.data.database.MonolithDatabase
import com.aowen.monolith.data.database.dao.ClaimedPlayerDao
import com.aowen.monolith.data.database.dao.FavoriteBuildDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesFavoriteBuildDao(
        database: MonolithDatabase
    ): FavoriteBuildDao = database.favoriteBuildListItemDao()

    @Provides
    fun providesClaimedPlayerDao(
        database: MonolithDatabase
    ): ClaimedPlayerDao = database.claimedPlayerDao()
}