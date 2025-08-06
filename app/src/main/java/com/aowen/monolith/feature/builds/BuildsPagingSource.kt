package com.aowen.monolith.feature.builds

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aowen.monolith.data.repository.builds.BuildRepository
import com.aowen.monolith.network.getOrThrow
import com.aowen.monolith.ui.model.BuildListItemUiMapper
import com.aowen.monolith.ui.model.BuildUiListItem

const val PAGE_SIZE = 10

class BuildsPagingSource(
    private val name: String? = null,
    private val role: String? = null,
    private val order: String? = null,
    private val heroId: Long? = null,
    private val skillOrder: Int? = null,
    private val currentVersion: Int? = null,
    private val modules: Int? = null,
    private val omedaCityBuildRepository: BuildRepository,
    private val buildListItemUiMapper: BuildListItemUiMapper
) : PagingSource<Int, BuildUiListItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BuildUiListItem> {
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
                data = response.map { buildListItemUiMapper.buildFrom(it) },
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, BuildUiListItem>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
}