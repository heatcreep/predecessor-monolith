package com.aowen.monolith.data.repository.matches.di

import com.aowen.monolith.data.repository.matches.MatchRepository
import com.aowen.monolith.data.repository.matches.OmedaCityMatchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MatchRepositoryModule {

    @Binds
    fun bindsMatchRepository(impl: OmedaCityMatchRepository): MatchRepository
}