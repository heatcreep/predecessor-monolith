package com.aowen.predcompanion.core.network

import com.aowen.predcompanion.core.network.apollo.AuthedUserQuery
import com.aowen.predcompanion.core.network.apollo.BuildsQuery
import com.aowen.predcompanion.core.network.apollo.GuideByIdQuery
import com.aowen.predcompanion.core.network.apollo.HeroesQuery
import com.aowen.predcompanion.core.network.apollo.ItemsQuery
import com.aowen.predcompanion.core.network.apollo.PlayerQuery
import com.aowen.predcompanion.core.network.apollo.SearchPlayersQuery
import com.apollographql.apollo.api.ApolloResponse

interface PredGGNetworkDataSource {
    suspend fun getUser(): ApolloResponse<AuthedUserQuery.Data>
    suspend fun getAllItems(): ApolloResponse<ItemsQuery.Data>
    suspend fun getAllHeroes(): ApolloResponse<HeroesQuery.Data>
    suspend fun searchPlayers(search: String): ApolloResponse<SearchPlayersQuery.Data>
    suspend fun getPlayer(playerId: String): ApolloResponse<PlayerQuery.Data>
    suspend fun getBuilds(limit: Int, offset: Int): ApolloResponse<BuildsQuery.Data>
    suspend fun getBuildById(buildId: String): ApolloResponse<GuideByIdQuery.Data>
}
