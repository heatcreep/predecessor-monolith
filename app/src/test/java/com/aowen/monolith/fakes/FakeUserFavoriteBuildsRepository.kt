package com.aowen.monolith.fakes

import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.network.FavoriteBuildsState
import com.aowen.monolith.network.UserFavoriteBuildsRepository
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserFavoriteBuildsRepository() : UserFavoriteBuildsRepository {

    override val favoriteBuildsState: MutableStateFlow<FavoriteBuildsState>
        get() {
            return MutableStateFlow(FavoriteBuildsState.Empty)
        }

    override suspend fun fetchFavoriteBuildIds(): Result<List<Int>> {
        return Result.success(listOf(1))
    }

    override suspend fun addFavoriteBuild(buildDetails: BuildListItem) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavoriteBuild(buildId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun removeAllFavoriteBuilds() {
        TODO("Not yet implemented")
    }
}