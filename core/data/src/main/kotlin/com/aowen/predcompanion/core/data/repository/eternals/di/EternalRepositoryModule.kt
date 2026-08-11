package com.aowen.predcompanion.core.data.repository.eternals.di

import com.aowen.predcompanion.core.data.repository.eternals.EternalRepository
import com.aowen.predcompanion.core.data.repository.eternals.PredGGEternalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface EternalRepositoryModule {

    @Binds
    fun bindsEternalRepository(impl: PredGGEternalRepository): EternalRepository
}