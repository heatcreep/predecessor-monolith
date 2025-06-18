package com.aowen.monolith.feature.builds

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.repository.builds.BuildRepository
import com.aowen.monolith.network.getOrThrow

const val PAGE_SIZE = 10

class BuildsPagingSource(
    private val name: String? = null,
    private val role: String? = null,
    private val order: String? = null,
    private val heroId: Int? = null,
    private val skillOrder: Int? = null,
    private val currentVersion: Int? = null,
    private val modules: Int? = null,
    private val omedaCityBuildRepository: BuildRepository
) : PagingSource<Int, BuildListItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BuildListItem> {
        return try {
            val page = params.key ?: 1
            val response = omedaCityBuildRepository.fetchAllBuilds(
                name = name,
                role = role,
                order = order,
                heroId = heroId,
                skillOrder = skillOrder,
                currentVersion = currentVersion,
                modules = modules,
                page = page
            ).getOrThrow()
            val nextKey = if(response.isEmpty()) null else page + 1

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