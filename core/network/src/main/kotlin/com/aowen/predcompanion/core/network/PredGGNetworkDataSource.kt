package com.aowen.predcompanion.core.network

import com.aowen.predcompanion.core.network.apollo.AuthedUserQuery
import com.aowen.predcompanion.core.network.apollo.HeroesQuery
import com.aowen.predcompanion.core.network.apollo.ItemsQuery
import com.apollographql.apollo.api.ApolloResponse

interface PredGGNetworkDataSource {
    suspend fun getUser(): ApolloResponse<AuthedUserQuery.Data>
    suspend fun getAllItems(): ApolloResponse<ItemsQuery.Data>
    suspend fun getAllHeroes(): ApolloResponse<HeroesQuery.Data>
}
