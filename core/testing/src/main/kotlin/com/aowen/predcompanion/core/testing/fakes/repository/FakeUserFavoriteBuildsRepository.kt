package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.data.model.mapper.BuildUiListItem
import com.aowen.predcompanion.core.data.repository.user.FavoriteBuildsState
import com.aowen.predcompanion.core.data.repository.user.UserFavoriteBuildsRepository
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserFavoriteBuildsRepository() : UserFavoriteBuildsRepository {

    override val favoriteBuildsState: MutableStateFlow<FavoriteBuildsState>
        get() {
            return MutableStateFlow(FavoriteBuildsState.Empty)
        }

    override suspend fun fetchFavoriteBuildIds(): Result<List<Int>> {
        return Result.success(listOf(1))
    }

    override suspend fun addFavoriteBuild(buildDetails: BuildUiListItem) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavoriteBuild(buildId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun removeAllFavoriteBuilds() {
        TODO("Not yet implemented")
    }
}