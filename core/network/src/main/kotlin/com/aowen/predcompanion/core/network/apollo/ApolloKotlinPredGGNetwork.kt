package com.aowen.predcompanion.core.network.apollo

import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.aowen.predcompanion.core.network.apollo.type.GuideFilterInput
import com.aowen.predcompanion.core.network.apollo.type.GuideKey
import com.aowen.predcompanion.core.network.apollo.type.GuideOrderInput
import com.aowen.predcompanion.core.network.apollo.type.MatchKey
import com.aowen.predcompanion.core.network.apollo.type.PlayerFilterInput
import com.aowen.predcompanion.core.network.apollo.type.PlayerKey
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Optional
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApolloKotlinPredGGNetwork @Inject constructor(
    private val apolloClient: ApolloClient
) : PredGGNetworkDataSource {

    // USER -->
    override suspend fun getCurrentUser(): ApolloResponse<AuthedUserQuery.Data> =
        apolloClient.query(AuthedUserQuery()).execute()

    // PLAYER -->
    override suspend fun getPlayer(playerId: String): ApolloResponse<GetPlayerQuery.Data> =
        apolloClient.query(GetPlayerQuery(PlayerKey(uuid = Optional.present(playerId)))).execute()

    // ITEMS
    override suspend fun getAllItems(): ApolloResponse<ItemsQuery.Data> =
        apolloClient.query(ItemsQuery()).execute()

    // HEROES
    override suspend fun getAllHeroes(): ApolloResponse<HeroesQuery.Data> =
        apolloClient.query(HeroesQuery()).execute()

    // MATCHES
    override suspend fun getMatchById(matchId: String): ApolloResponse<MatchByIdQuery.Data> =
        apolloClient.query(
            MatchByIdQuery(
                MatchKey(
                    id = Optional.present(matchId)
                )
            )
        ).execute()

    // PLAYER SEARCH
    override suspend fun searchPlayers(
        search: String,
        limit: Int?,
        offset: Int?
    ): ApolloResponse<SearchPlayersQuery.Data> =
        apolloClient.query(
            SearchPlayersQuery(
                filter = PlayerFilterInput(search = search),
                limit = Optional.presentIfNotNull(limit),
                offset = Optional.presentIfNotNull(offset)
            )
        ).execute()

    // BUILDS
    override suspend fun getBuilds(
        filter: GuideFilterInput?,
        order: GuideOrderInput?,
        limit: Int?,
        offset: Int?
    ): ApolloResponse<BuildsQuery.Data> =
        apolloClient.query(
            BuildsQuery(
                filter = Optional.presentIfNotNull(filter),
                order = Optional.presentIfNotNull(order),
                limit = Optional.presentIfNotNull(limit),
                offset = Optional.presentIfNotNull(offset)
            )
        ).execute()

    override suspend fun getBuildById(buildId: String): ApolloResponse<GetGuideQuery.Data> =
        apolloClient.query(
            GetGuideQuery(GuideKey(id = Optional.present(buildId)))
        ).execute()

}
