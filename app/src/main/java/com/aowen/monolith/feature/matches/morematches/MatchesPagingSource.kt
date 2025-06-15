package com.aowen.monolith.feature.matches.morematches

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.MatchesDetails

const val PAGE_SIZE = 30

class MatchesPagingSource(
    private val getMatchesById: suspend (Int, Int) -> MatchesDetails
) : PagingSource<Int, MatchDetails>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchDetails> {
        return try {
            val page = params.key ?: 1
            val response = getMatchesById(page, PAGE_SIZE)
            val nextKey = if (response.matches.isEmpty()) null else {
                page + 1
            }

            LoadResult.Page(
                data = response.matches,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MatchDetails>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
}