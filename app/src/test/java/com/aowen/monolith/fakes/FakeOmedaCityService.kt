package com.aowen.predcompanion.fakes

import com.aowen.predcompanion.data.BuildDto
import com.aowen.predcompanion.data.HeroDto
import com.aowen.predcompanion.data.HeroStatisticsResponseDto
import com.aowen.predcompanion.data.ItemDto
import com.aowen.monolith.data.MatchDto
import com.aowen.predcompanion.data.MatchesDto
import com.aowen.predcompanion.data.PlayerDto
import com.aowen.predcompanion.data.PlayerHeroStatsResponseDto
import com.aowen.predcompanion.data.PlayerStatsDto
import com.aowen.predcompanion.fakes.data.fakeBuildDto
import com.aowen.predcompanion.fakes.data.fakeHeroDto
import com.aowen.predcompanion.fakes.data.fakeHeroStatisticsDto
import com.aowen.predcompanion.fakes.data.fakeItemDto
import com.aowen.predcompanion.fakes.data.fakeMatchDto
import com.aowen.predcompanion.fakes.data.fakePlayerDto
import com.aowen.predcompanion.fakes.data.fakePlayerHeroStatsDto
import com.aowen.predcompanion.fakes.data.fakePlayerStatsDto
import com.aowen.predcompanion.core.network.OmedaCityService
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeOmedaCityService(private val resCode: Int? = null) : OmedaCityService {
    override suspend fun getPlayerById(playerId: String): Response<PlayerDto> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody())
            200 -> Response.success(fakePlayerDto)
            else -> throw Exception("Something went wrong")

        }
    }

    override suspend fun getPlayerHeroStatsById(playerId: String): Response<PlayerHeroStatsResponseDto> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(
                PlayerHeroStatsResponseDto(
                    heroStatistics = listOf(
                        fakePlayerHeroStatsDto
                    )
                )
            )

            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getPlayerStatsById(playerId: String): Response<PlayerStatsDto> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(fakePlayerStatsDto)
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getPlayerMatchesById(
        playerId: String,
        perPage: Int?,
        timeFrame: String?,
        heroId: Int?,
        role: String?,
        playerName: String?,
        page: Int?
    ): Response<MatchesDto> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(
                MatchesDto(
                    matches = listOf(fakeMatchDto),
                    cursor = "cursor"
                )
            )

            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getMatchById(matchId: String): Response<MatchDto> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(fakeMatchDto)
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getPlayersByName(
        playerName: String,
        includeInactive: Int
    ): Response<List<PlayerDto>> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(listOf(fakePlayerDto))
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getAllHeroes(): Response<List<HeroDto>> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(listOf(fakeHeroDto))
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getHeroByName(heroName: String): Response<HeroDto> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody())
            200 -> Response.success(fakeHeroDto)
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getAllHeroStatistics(timeFrame: String?): Response<HeroStatisticsResponseDto> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(
                HeroStatisticsResponseDto(
                    listOf(fakeHeroStatisticsDto)
                )
            )
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getHeroStatisticsById(heroIds: String): Response<HeroStatisticsResponseDto> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(
                HeroStatisticsResponseDto(
                    heroStatistics = listOf(
                        fakeHeroStatisticsDto
                    )
                )
            )

            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getAllItems(): Response<List<ItemDto>> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(listOf(fakeItemDto))
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getItemByName(itemName: String): Response<ItemDto> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(fakeItemDto)
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getBuilds(
        name: String?,
        role: String?,
        order: String?,
        heroId: Long?,
        skillOrder: Int?,
        modules: Int?,
        currentVersion: Int?,
        page: Int?
    ): Response<List<BuildDto>> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(listOf(fakeBuildDto))
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getBuildById(buildId: String): Response<BuildDto> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(fakeBuildDto)
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun deeplinkToNewBuild(
        title: String,
        description: String,
        role: String,
        heroId: Int,
        crestId: Int,
        itemIds: Map<String, Int>,
        skillOrder: Map<String, Int>,
        modules: Map<String, String>
    ) {
        TODO("Not yet implemented")
    }
}