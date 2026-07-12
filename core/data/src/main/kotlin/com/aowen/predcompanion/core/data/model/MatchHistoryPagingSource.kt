package com.aowen.predcompanion.core.data.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aowen.predcompanion.core.model.data.MatchHistoryItem
import com.aowen.predcompanion.core.network.apollo.CurrentUserMatchHistoryQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.DefaultApolloException

class MatchHistoryPagingSource(
    private val client: ApolloClient
) : PagingSource<Int, MatchHistoryItem>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MatchHistoryItem> {
        val offset = params.key ?: 0
        val limit = params.loadSize
        return try {
            val response = client
                .query(CurrentUserMatchHistoryQuery(limit = limit, offset = offset))
                .execute()
            if (response.hasErrors()) return LoadResult.Error(
                DefaultApolloException(
                    response.errors?.firstOrNull()?.message ?: "GraphQL error"
                )
            )
            val matches = response.data?.currentUser?.players?.firstOrNull()?.matchesPaginated
                ?: return LoadResult.Error(IllegalStateException("No matches found"))

            val matchDetails = matches.matchResultsFragment.results.map {
                it.asMatchHistoryItem()
            }
            val totalCount = matches.totalCount

            val nextOffset = (offset + limit).takeIf { it < totalCount }
            val previousOffset = (offset - limit).takeIf { it >= 0 }
            return LoadResult.Page(
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