package com.aowen.predcompanion.core.data.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aowen.predcompanion.core.model.data.MatchHistoryItem
import com.aowen.predcompanion.core.network.apollo.CurrentUserMatchHistoryQuery
import com.aowen.predcompanion.core.network.apollo.GetPlayerMatchHistoryQuery
import com.aowen.predcompanion.core.network.apollo.type.PlayerKey
import com.aowen.predcompanion.core.network.apollo.type.PlayerMatchesFilterInput
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.exception.DefaultApolloException

sealed interface MatchHistoryTarget {
    data object CurrentUser : MatchHistoryTarget
    data class SpecificPlayer(val playerKey: PlayerKey) : MatchHistoryTarget
}

class MatchHistoryPagingSource(
    private val client: ApolloClient,
    private val target: MatchHistoryTarget,
    private val filter: PlayerMatchesFilterInput? = null
) : PagingSource<Int, MatchHistoryItem>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MatchHistoryItem> {
        val offset = params.key ?: 0
        val limit = params.loadSize

        return try {
            val fragment = when (target) {
                is MatchHistoryTarget.CurrentUser -> {
                    val response = client
                        .query(
                            CurrentUserMatchHistoryQuery(
                                limit = limit,
                                offset = offset,
                                filter = Optional.presentIfNotNull(filter)
                            )
                        )
                        .execute()

                    if (response.hasErrors()) {
                        return LoadResult.Error(
                            DefaultApolloException(
                                response.errors?.firstOrNull()?.message ?: "GraphQL error"
                            )
                        )
                    }

                    response.data?.currentUser?.players?.firstOrNull()?.matchesPaginatedFragment
                }

                is MatchHistoryTarget.SpecificPlayer -> {
                    val response = client
                        .query(
                            GetPlayerMatchHistoryQuery(
                                playerKey = target.playerKey,
                                limit = limit,
                                offset = offset,
                                filter = Optional.presentIfNotNull(filter)
                            )
                        )
                        .execute()

                    if (response.hasErrors()) {
                        return LoadResult.Error(
                            DefaultApolloException(
                                response.errors?.firstOrNull()?.message ?: "GraphQL error"
                            )
                        )
                    }

                    response.data?.player?.matchesPaginatedFragment
                }
            } ?: return LoadResult.Error(IllegalStateException("No matches found"))

            val matches = fragment.matchesPaginated
                ?: return LoadResult.Error(IllegalStateException("No matches found"))

            val matchDetails = matches.matchResultsFragment.results.map {
                it.asMatchHistoryItem()
            }
            val totalCount = matches.totalCount

            val nextOffset = (offset + limit).takeIf { it < totalCount }
            val previousOffset = (offset - limit).takeIf { it >= 0 }

            LoadResult.Page(
                data = matchDetails,
                prevKey = previousOffset,
                nextKey = nextOffset
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MatchHistoryItem>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
}