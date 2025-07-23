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

    companion object {
        val buildListItem1 = BuildListItem(
            id = 1,
            heroId = 24,
            author = "Author 1",
            title = "Title 1",
            description = "Description 1",
            role = "Role 1",
            crest = 1,
            version = "1.0.0",
            upvotes = 10,
            downvotes = 5,
            buildItems = listOf(1, 2, 3),
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