package com.aowen.predcompanion.core.data.repository.builds.di

import com.aowen.predcompanion.core.data.repository.builds.BuildRepository
import com.aowen.predcompanion.core.data.repository.builds.OmedaCityBuildRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BuildRepositoryModule {

    @Binds
    fun bindsBuildRepository(impl: OmedaCityBuildRepository): BuildRepository
}