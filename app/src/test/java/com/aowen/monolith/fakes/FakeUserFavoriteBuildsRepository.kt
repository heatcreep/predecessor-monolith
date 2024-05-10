package com.aowen.monolith.fakes

import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.FavoriteBuildListItem
import com.aowen.monolith.network.UserFavoriteBuildsRepository

class FakeUserFavoriteBuildsRepository : UserFavoriteBuildsRepository {

    companion object {
        val buildListItem1 = FavoriteBuildListItem(
            buildId = 1,
            heroId = 24,
            author = "Author 1",
            title = "Title 1",
            description = "Description 1",
            role = "Role 1",
            crestId = 1,
            gameVersion = "1.0.0",
            upvotesCount = 10,
            downvotesCount = 5,
            itemIds = listOf(1, 2, 3),
            createdAt = "2021-01-01"
        )
    }
    override suspend fun fetchFavoriteBuilds(): Result<List<FavoriteBuildListItem>> {
        return Result.success(listOf(buildListItem1))
    }

    override suspend fun addFavoriteBuild(buildDetails: BuildListItem) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavoriteBuild(buildId: Int) {
        TODO("Not yet implemented")
    }
}