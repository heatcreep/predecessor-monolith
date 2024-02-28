package com.aowen.monolith.ui.screens.builds

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.network.OmedaCityRepository

const val PAGE_SIZE = 10

class BuildsPagingSource(
    private val name: String? = null,
    private val role: String? = null,
    private val order: String? = null,
    private val heroId: Int? = null,
    private val skillOrder: Int? = null,
    private val modules: Int? = null,
    private val repository: OmedaCityRepository
) : PagingSource<Int, BuildListItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BuildListItem> {
        return try {
            val page = params.key ?: 1
            val response = repository.fetchAllBuilds(
                name = name,
                role = role,
                order = order,
                heroId = heroId,
                skillOrder = skillOrder,
                modules = modules,
                page = page
            ).getOrNull() ?: emptyList()
            val nextKey =
                if(response.isEmpty()) null else page + 1

            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, BuildListItem>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
}