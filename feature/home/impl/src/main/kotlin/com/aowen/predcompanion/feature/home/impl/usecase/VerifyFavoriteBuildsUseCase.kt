package com.aowen.predcompanion.feature.home.impl.usecase

import com.aowen.predcompanion.core.data.repository.builds.BuildRepository
import com.aowen.predcompanion.core.data.repository.user.UserFavoriteBuildsRepository
import com.aowen.predcompanion.data.FavoriteBuildListItem
import com.aowen.predcompanion.data.asFavoriteBuildListItem
import com.aowen.predcompanion.core.network.Resource
import javax.inject.Inject

/**
 * Use case to verify and fetch favorite builds for the user in case
 * a favorited build has been removed or is no longer available on
 * Omeda City.
 */
class VerifyFavoriteBuildsUseCase @Inject constructor(
    private val userFavoriteBuildsRepository: UserFavoriteBuildsRepository,
    private val buildRepository: BuildRepository
) {

    suspend operator fun invoke(): Result<List<FavoriteBuildListItem>> {
        val buildIds = userFavoriteBuildsRepository.fetchFavoriteBuildIds()

        return buildIds.fold(
            onSuccess = { favoriteBuildIds ->
                Result.success(favoriteBuildIds.mapNotNull { id ->
                    when (val buildResponse = buildRepository.fetchBuildById(id.toString())) {
                        is Resource.Success -> {
                            buildResponse.data.asFavoriteBuildListItem()
                        }
                        else -> {
                            userFavoriteBuildsRepository.removeFavoriteBuild(id)
                            null
                        }
                    }
                })
            },
            onFailure = { exception ->
                Result.failure(exception)
            }
        )
    }
}