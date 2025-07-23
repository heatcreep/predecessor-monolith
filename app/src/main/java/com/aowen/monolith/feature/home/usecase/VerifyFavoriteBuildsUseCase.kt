package com.aowen.monolith.feature.home.usecase

import com.aowen.monolith.data.FavoriteBuildListItem
import com.aowen.monolith.data.asFavoriteBuildListItem
import com.aowen.monolith.data.repository.builds.BuildRepository
import com.aowen.monolith.network.Resource
import com.aowen.monolith.network.UserFavoriteBuildsRepository
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
                    val buildResponse = buildRepository.fetchBuildById(id.toString())
                    when (buildResponse) {
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