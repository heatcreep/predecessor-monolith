package com.aowen.monolith.ui.screens.matches

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.network.OmedaCityRepository

const val PAGE_SIZE = 30

class MatchesPagingSource(
    private val playerId: String,
    private val timeFrame: String? = null,
    private val heroId: Int? = null,
    private val role: String? = null,
    private val playerName: String? = null,
    private val repository: OmedaCityRepository
) : PagingSource<Int, MatchDetails>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchDetails> {
        return try {
            val page = params.key ?: 1
            val response = repository.fetchMatchesById(
                playerId = playerId,
                heroId = heroId,
                role = role,
                timeFrame = timeFrame,
                playerName = playerName,
                page = page,
                perPage = PAGE_SIZE
            ).getOrThrow()
            val nextKey = if (response?.matches.isNullOrEmpty()) null else {
                page + 1
            }

            LoadResult.Page(
                data = response?.matches ?: emptyList(),
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