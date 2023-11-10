package com.aowen.monolith.fakes

import com.aowen.monolith.data.HeroDto
import com.aowen.monolith.data.HeroStatisticsResponseDto
import com.aowen.monolith.data.ItemDto
import com.aowen.monolith.data.MatchDto
import com.aowen.monolith.data.MatchesDto
import com.aowen.monolith.data.PlayerDto
import com.aowen.monolith.data.PlayerHeroStatsResponseDto
import com.aowen.monolith.data.PlayerStatsDto
import com.aowen.monolith.fakes.data.fakeHeroDto
import com.aowen.monolith.fakes.data.fakeHeroStatisticsDto
import com.aowen.monolith.fakes.data.fakeItemDto
import com.aowen.monolith.fakes.data.fakeMatchDto
import com.aowen.monolith.fakes.data.fakePlayerDto
import com.aowen.monolith.fakes.data.fakePlayerHeroStatsDto
import com.aowen.monolith.fakes.data.fakePlayerStatsDto
import com.aowen.monolith.network.OmedaCityService
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

    override suspend fun getPlayerMatchesById(playerId: String): Response<MatchesDto> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(
                MatchesDto(
                    matches = listOf(fakeMatchDto)
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

    override suspend fun getPlayersByName(playerName: String): Response<List<PlayerDto>> {
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
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(fakeHeroDto)
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
}