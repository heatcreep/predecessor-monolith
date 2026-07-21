package com.aowen.predcompanion.core.network

import com.aowen.predcompanion.core.network.apollo.AuthedUserQuery
import com.aowen.predcompanion.core.network.apollo.GetPlayerQuery
import com.aowen.predcompanion.core.network.apollo.HeroesQuery
import com.aowen.predcompanion.core.network.apollo.ItemsQuery
import com.apollographql.apollo.api.ApolloResponse

interface PredGGNetworkDataSource {
    suspend fun getCurrentUser(): ApolloResponse<AuthedUserQuery.Data>
    suspend fun getPlayer(playerId: String): ApolloResponse<GetPlayerQuery.Data>
    suspend fun getAllItems(): ApolloResponse<ItemsQuery.Data>
    suspend fun getAllHeroes(): ApolloResponse<HeroesQuery.Data>
}
