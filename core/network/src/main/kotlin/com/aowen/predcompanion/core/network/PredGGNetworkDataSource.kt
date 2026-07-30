package com.aowen.predcompanion.core.network

import com.aowen.predcompanion.core.network.apollo.AuthedUserQuery
import com.aowen.predcompanion.core.network.apollo.BuildsQuery
import com.aowen.predcompanion.core.network.apollo.GetGuideQuery
import com.aowen.predcompanion.core.network.apollo.GetPlayerQuery
import com.aowen.predcompanion.core.network.apollo.HeroesQuery
import com.aowen.predcompanion.core.network.apollo.ItemsQuery
import com.aowen.predcompanion.core.network.apollo.MatchByIdQuery
import com.aowen.predcompanion.core.network.apollo.SearchPlayersQuery
import com.aowen.predcompanion.core.network.apollo.type.GuideFilterInput
import com.aowen.predcompanion.core.network.apollo.type.GuideOrderInput
import com.apollographql.apollo.api.ApolloResponse

interface PredGGNetworkDataSource {
    suspend fun getCurrentUser(): ApolloResponse<AuthedUserQuery.Data>
    suspend fun getPlayer(playerId: String): ApolloResponse<GetPlayerQuery.Data>
    suspend fun getAllItems(): ApolloResponse<ItemsQuery.Data>
    suspend fun getAllHeroes(): ApolloResponse<HeroesQuery.Data>
    suspend fun getMatchById(matchId: String): ApolloResponse<MatchByIdQuery.Data>
    suspend fun searchPlayers(
        search: String,
        limit: Int? = null,
        offset: Int? = null
    ): ApolloResponse<SearchPlayersQuery.Data>

    suspend fun getBuilds(
        filter: GuideFilterInput? = null,
        order: GuideOrderInput? = null,
        limit: Int? = null,
        offset: Int? = null
    ): ApolloResponse<BuildsQuery.Data>

    suspend fun getBuildById(buildId: String): ApolloResponse<GetGuideQuery.Data>
}
