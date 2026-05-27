package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.model.data.asBuildListItem
import com.aowen.predcompanion.core.data.repository.builds.BuildRepository
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHeroBuild

private var pageCount = 1

fun resetPageCount() {
    pageCount = 1
}

class FakeOmedaCityBuildRepository : BuildRepository {

    companion object {
        val heroBuild1 = HeroBuild(
            id = 1,
            heroId = 24,
            author = "Author 1",
            title = "Title 1",
            description = "Description 1",
            role = "Role 1",
            crestId = 1,
            version = "1.0.0",
            upvotes = 10,
            downvotes = 5,
            buildItemIds = listOf(1, 2, 3),
            createdAt = "2021-01-01"
        )
    }

    override suspend fun fetchAllBuilds(
        name: String?,
        role: String?,
        order: String?,
        heroId: Long?,
        skillOrder: Int?,
        currentVersion: Int?,
        modules: Int?,
        page: Int?
    ): Resource<List<HeroBuild>> {
        pageCount++
        return if (pageCount >= 4) {
            Resource.Success(emptyList())
        } else {
            Resource.Success(List(10) { fakeNetworkHeroBuild.asBuildListItem() })
        }
    }

    override suspend fun fetchBuildById(buildId: String): Resource<HeroBuild> =
        Resource.Success(fakeNetworkHeroBuild.asBuildListItem())
}