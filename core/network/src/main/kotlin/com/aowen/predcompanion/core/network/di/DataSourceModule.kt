package com.aowen.predcompanion.core.network.di

import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.aowen.predcompanion.core.network.apollo.ApolloKotlinPredGGNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourceModule {

    @Binds
    fun bindsPredGGNetworkDataSource(
        impl: ApolloKotlinPredGGNetwork
    ): PredGGNetworkDataSource
}
