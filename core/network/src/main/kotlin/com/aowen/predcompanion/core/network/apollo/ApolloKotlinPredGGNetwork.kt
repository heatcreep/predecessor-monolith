package com.aowen.predcompanion.core.network.apollo

import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
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
}
