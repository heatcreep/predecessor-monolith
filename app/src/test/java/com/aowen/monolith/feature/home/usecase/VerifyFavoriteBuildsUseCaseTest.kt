package com.aowen.monolith.feature.home.usecase

import com.aowen.monolith.fakes.repo.FakeOmedaCityBuildRepository
import com.aowen.monolith.network.Resource
import com.aowen.monolith.network.UserFavoriteBuildsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Test

class VerifyFavoriteBuildsUseCaseTest {

    private lateinit var useCase: VerifyFavoriteBuildsUseCase

    val mockBuildItemResponse = FakeOmedaCityBuildRepository.buildListItem1

    private var favoriteBuildsRepository: UserFavoriteBuildsRepository = mockk {
        coEvery { fetchFavoriteBuildIds() } returns Result.success(listOf(123))
        coEvery { removeFavoriteBuild(any()) } just runs
    }

    private var buildRepository: FakeOmedaCityBuildRepository = mockk {
        coEvery { fetchBuildById(any()) } returns Resource.Success(data = mockBuildItemResponse)
    }

    @Test
    fun `invoke should return empty list when no favorite builds exist`() = runTest {
        favoriteBuildsRepository = mockk {
            coEvery { fetchFavoriteBuildIds() } returns Result.success(emptyList())
        }
        useCase = VerifyFavoriteBuildsUseCase(
            userFavoriteBuildsRepository = favoriteBuildsRepository,
            buildRepository = buildRepository
        )
        val result = useCase.invoke()
        assert(result.isSuccess)
        assert(result.getOrThrow().isEmpty())
    }

    @Test
    fun `invoke should return list of favorite builds when they exist`() = runTest {
        useCase = VerifyFavoriteBuildsUseCase(
            userFavoriteBuildsRepository = favoriteBuildsRepository,
            buildRepository = buildRepository
        )
        val result = useCase.invoke()
        assert(result.isSuccess)
        assert(result.getOrThrow().isNotEmpty())
        assert(result.getOrThrow().first().buildId == mockBuildItemResponse.id)
    }

    @Test
    fun `invoke should remove favorite build when it no longer exists`() = runTest {
        coEvery { buildRepository.fetchBuildById(any()) } returns Resource.NetworkError(
            code = 404,
            errorMessage = "Build not found"
        )

        useCase = VerifyFavoriteBuildsUseCase(
            userFavoriteBuildsRepository = favoriteBuildsRepository,
            buildRepository = buildRepository
        )

        val result = useCase.invoke()
        coVerify { favoriteBuildsRepository.removeFavoriteBuild(any()) }
        assert(result.isSuccess)
        assert(result.getOrThrow().isEmpty())
    }

    @Test
    fun `invoke should filter out removed favorite builds when they no longer exists`() = runTest {
        coEvery { buildRepository.fetchBuildById(any()) } returns Resource.NetworkError(
            code = 404,
            errorMessage = "Build not found"
        )

        useCase = VerifyFavoriteBuildsUseCase(
            userFavoriteBuildsRepository = favoriteBuildsRepository,
            buildRepository = buildRepository
        )

        val result = useCase.invoke()
        assert(result.isSuccess)
        assert(result.getOrThrow().isEmpty())
    }

    @Test
    fun `invoke should return failure when getting buildIds fails`() = runTest {
        coEvery { favoriteBuildsRepository.fetchFavoriteBuildIds() } returns Result.failure(Exception("Network error"))

        useCase = VerifyFavoriteBuildsUseCase(
            userFavoriteBuildsRepository = favoriteBuildsRepository,
            buildRepository = buildRepository
        )

        val result = useCase.invoke()
        assert(result.isFailure)
        assert(result.exceptionOrNull()?.message == "Network error")
    }
}