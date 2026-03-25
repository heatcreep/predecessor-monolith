package com.aowen.monolith.data.repository.players.di

import com.aowen.monolith.data.repository.players.OmedaCityPlayerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PlayerRepositoryModule {

    @Binds
    fun bindsPlayerRepository(impl: OmedaCityPlayerRepository): PlayerRepository
}