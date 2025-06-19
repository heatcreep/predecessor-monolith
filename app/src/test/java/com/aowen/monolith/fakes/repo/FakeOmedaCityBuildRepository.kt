package com.aowen.monolith.fakes.repo

import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.asBuildListItem
import com.aowen.monolith.data.repository.builds.BuildRepository
import com.aowen.monolith.fakes.data.fakeBuildDto
import com.aowen.monolith.network.Resource

private var pageCount = 1

fun resetPageCount() {
    pageCount = 1
}

class FakeOmedaCityBuildRepository : BuildRepository {

    override suspend fun fetchAllBuilds(
        name: String?,
        role: String?,
        order: String?,
        heroId: Long?,
        skillOrder: Int?,
        currentVersion: Int?,
        modules: Int?,
        page: Int?
    ): Resource<List<BuildListItem>> {
        pageCount++
        return if (pageCount >= 4) {
            Resource.Success(emptyList())
        } else {
            Resource.Success(List(10) { fakeBuildDto.asBuildListItem() })
        }
    }

    override suspend fun fetchBuildById(buildId: String): Resource<BuildListItem> =
        Resource.Success(fakeBuildDto.asBuildListItem())
}