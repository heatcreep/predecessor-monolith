package com.aowen.predcompanion.core.testing.fakes.service

import com.aowen.predcompanion.core.network.PredCompanionNetworkDataSource
import com.aowen.predcompanion.core.network.model.NetworkHero
import com.aowen.predcompanion.core.network.model.NetworkHeroBuild
import com.aowen.predcompanion.core.network.model.NetworkHeroStatisticsResponse
import com.aowen.predcompanion.core.network.model.NetworkItem
import com.aowen.predcompanion.core.network.model.NetworkMatch
import com.aowen.predcompanion.core.network.model.NetworkMatchesList
import com.aowen.predcompanion.core.network.model.NetworkPlayer
import com.aowen.predcompanion.core.network.model.NetworkPlayerHeroStatsResponse
import com.aowen.predcompanion.core.network.model.NetworkPlayerStats
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHero
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHeroBuild
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkHeroStatistics
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem2
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem3
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem4
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkMatch
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkPlayer
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkPlayerHeroStats
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkPlayerStats
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class TestPredCompanionNetworkDataSource(private val resCode: Int? = null) : PredCompanionNetworkDataSource {
    override suspend fun getPlayerById(playerId: String): Response<NetworkPlayer> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody())
            200 -> Response.success(fakeNetworkPlayer)
            else -> throw Exception("Something went wrong")

        }
    }

    override suspend fun getPlayerHeroStatsById(playerId: String): Response<NetworkPlayerHeroStatsResponse> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(
                NetworkPlayerHeroStatsResponse(
                    heroStatistics = listOf(
                        fakeNetworkPlayerHeroStats
                    )
                )
            )

            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getPlayerStatsById(playerId: String): Response<NetworkPlayerStats> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(fakeNetworkPlayerStats)
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
    ): Response<NetworkMatchesList> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(
                NetworkMatchesList(
                    matches = listOf(fakeNetworkMatch),
                    cursor = "cursor"
                )
            )

            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getMatchById(matchId: String): Response<NetworkMatch> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(fakeNetworkMatch)
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getPlayersByName(
        playerName: String,
        includeInactive: Int
    ): Response<List<NetworkPlayer>> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(listOf(fakeNetworkPlayer))
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getAllHeroes(): Response<List<NetworkHero>> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(listOf(fakeNetworkHero))
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getHeroByName(heroName: String): Response<NetworkHero> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody())
            200 -> Response.success(fakeNetworkHero)
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getAllHeroStatistics(timeFrame: String?): Response<NetworkHeroStatisticsResponse> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(
                NetworkHeroStatisticsResponse(
                    listOf(fakeNetworkHeroStatistics)
                )
            )
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getHeroStatisticsById(heroIds: String): Response<NetworkHeroStatisticsResponse> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(
                NetworkHeroStatisticsResponse(
                    heroStatistics = listOf(
                        fakeNetworkHeroStatistics
                    )
                )
            )

            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getAllItems(): Response<List<NetworkItem>> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(listOf(fakeNetworkItem, fakeNetworkItem2, fakeNetworkItem3, fakeNetworkItem4
            ))
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getItemByName(itemName: String): Response<NetworkItem> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(fakeNetworkItem)
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
    ): Response<List<NetworkHeroBuild>> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(listOf(fakeNetworkHeroBuild))
            else -> throw Exception("Something went wrong")
        }
    }

    override suspend fun getBuildById(buildId: String): Response<NetworkHeroBuild> {
        return when (resCode) {
            404 -> Response.error(404, "Not Found".toResponseBody(null))
            200 -> Response.success(fakeNetworkHeroBuild)
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