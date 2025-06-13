package com.aowen.monolith.data.repository.items.di

import com.aowen.monolith.data.repository.items.ItemRepository
import com.aowen.monolith.data.repository.items.OmedaCityItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ItemRepositoryModule {

    @Binds
    fun bindsItemRepository(impl: OmedaCityItemRepository): ItemRepository
}