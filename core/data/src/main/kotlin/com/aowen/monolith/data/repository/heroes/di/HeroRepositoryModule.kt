package com.aowen.monolith.data.repository.heroes.di

import com.aowen.monolith.data.repository.heroes.HeroRepository
import com.aowen.monolith.data.repository.heroes.OmedaCityHeroRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface HeroRepositoryModule {

    @Binds
    fun bindsHeroRepository(impl: OmedaCityHeroRepository): HeroRepository
}