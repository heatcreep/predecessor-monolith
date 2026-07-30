package com.aowen.predcompanion.core.network.apollo

import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
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
    override suspend fun getUser(): ApolloResponse<AuthedUserQuery.Data> =
        apolloClient.query(AuthedUserQuery()).execute()

    // ITEMS
    override suspend fun getAllItems(): ApolloResponse<ItemsQuery.Data> =
        apolloClient.query(ItemsQuery()).execute()

    // HEROES
    override suspend fun getAllHeroes(): ApolloResponse<HeroesQuery.Data> =
        apolloClient.query(HeroesQuery()).execute()

    // PLAYERS
    override suspend fun searchPlayers(search: String): ApolloResponse<SearchPlayersQuery.Data> =
        apolloClient.query(SearchPlayersQuery(search)).execute()

    override suspend fun getPlayer(playerId: String): ApolloResponse<PlayerQuery.Data> =
        apolloClient.query(PlayerQuery(playerId)).execute()

    // BUILDS
    override suspend fun getBuilds(limit: Int, offset: Int): ApolloResponse<BuildsQuery.Data> =
        apolloClient.query(
            BuildsQuery(
                limit = Optional.present(limit),
                offset = Optional.present(offset)
            )
        ).execute()

    override suspend fun getBuildById(buildId: String): ApolloResponse<GuideByIdQuery.Data> =
        apolloClient.query(GuideByIdQuery(buildId)).execute()
}
