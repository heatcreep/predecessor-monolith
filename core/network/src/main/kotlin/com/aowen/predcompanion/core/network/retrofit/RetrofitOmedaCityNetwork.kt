package com.aowen.predcompanion.core.network.retrofit

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
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitOmedaCityNetworkApi {
    // Player Details
    @GET("/players/{player_id}.json")
    suspend fun getPlayerById(@Path("player_id") playerId: String): Response<NetworkPlayer>

    @GET("/players/{player_id}/hero_statistics.json")
    suspend fun getPlayerHeroStatsById(@Path("player_id") playerId: String): Response<NetworkPlayerHeroStatsResponse>

    // Player Stats
    @GET("players/{player_id}/statistics.json")
    suspend fun getPlayerStatsById(@Path("player_id") playerId: String): Response<NetworkPlayerStats>

    // Player Matches
    @GET("/players/{player_id}/matches.json")
    suspend fun getPlayerMatchesById(
        @Path("player_id") playerId: String,
        @Query("per_page") perPage: Int? = 10,
        @Query("time_frame") timeFrame: String? = null,
        @Query("filter[hero_id]") heroId: Int? = null,
        @Query("filter[role]") role: String? = null,
        @Query("filter[player_name]") playerName: String? = null,
        @Query("page") page: Int? = 1
    ): Response<NetworkMatchesList>

    @GET("/matches/{match_id}.json")
    suspend fun getMatchById(@Path("match_id") matchId: String): Response<NetworkMatch>

    // Find Players By Name
    @GET("players.json")
    suspend fun getPlayersByName(
        @Query("filter[name]") playerName: String,
        @Query("filter[include_inactive]") includeInactive: Int = 1
    ): Response<List<NetworkPlayer>>

    @GET("heroes.json")
    suspend fun getAllHeroes(): Response<List<NetworkHero>>

    @GET("heroes/{hero_name}.json")
    suspend fun getHeroByName(@Path("hero_name") heroName: String): Response<NetworkHero>

    @GET("dashboard/hero_statistics.json")
    suspend fun getAllHeroStatistics(@Query("time_frame") timeFrame: String?): Response<NetworkHeroStatisticsResponse>

    @GET("dashboard/hero_statistics.json")
    suspend fun getHeroStatisticsById(@Query("hero_ids") heroIds: String): Response<NetworkHeroStatisticsResponse>

    @GET("items.json")
    suspend fun getAllItems(): Response<List<NetworkItem>>

    @GET("items/{item_name}.json")
    suspend fun getItemByName(@Path("item_name") itemName: String): Response<NetworkItem>


    // Builds
    @GET("builds.json")
    suspend fun getBuilds(
        @Query("filter[name]") name: String? = null,
        @Query("filter[role]") role: String? = null,
        @Query("filter[order]") order: String? = "popular",
        @Query("filter[hero_id]") heroId: Long? = null,
        @Query("filter[skill_order]") skillOrder: Int? = null,
        @Query("filter[modules]") modules: Int? = null,
        @Query("filter[current_version]") currentVersion: Int? = null,
        @Query("page") page: Int? = 1,
    ): Response<List<NetworkHeroBuild>>

    @GET("builds/{build_id}.json")
    suspend fun getBuildById(
        @Path("build_id") buildId: String
    ): Response<NetworkHeroBuild>

    @GET("builds/new")
    suspend fun deeplinkToNewBuild(
        @Query("build[title]") title: String,
        @Query("build[description]") description: String,
        @Query("build[role]") role: String,
        @Query("build[hero_id]") heroId: Int,
        @Query("build[crest_id") crestId: Int,
        @QueryMap itemIds: Map<String, Int>,
        @QueryMap skillOrder: Map<String, Int>,
        @QueryMap modules: Map<String, String>,
    )
}

private const val BASE_URL = "https://omeda.city/"

@Singleton
class RetrofitOmedaCityNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>
) : PredCompanionNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory { okhttpCallFactory.get().newCall(it) }
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType())
        ).build()
        .create(RetrofitOmedaCityNetworkApi::class.java)

    override suspend fun getPlayerById(playerId: String): Response<NetworkPlayer> =
        networkApi.getPlayerById(playerId)

    override suspend fun getPlayerHeroStatsById(playerId: String): Response<NetworkPlayerHeroStatsResponse> =
        networkApi.getPlayerHeroStatsById(playerId)

    override suspend fun getPlayerStatsById(playerId: String): Response<NetworkPlayerStats> =
        networkApi.getPlayerStatsById(playerId)

    override suspend fun getPlayerMatchesById(
        playerId: String,
        perPage: Int?,
        timeFrame: String?,
        heroId: Int?,
        role: String?,
        playerName: String?,
        page: Int?
    ): Response<NetworkMatchesList> =
        networkApi.getPlayerMatchesById(
            playerId = playerId,
            perPage = perPage,
            timeFrame = timeFrame,
            heroId = heroId,
            role = role,
            playerName = playerName,
            page = page
        )

    override suspend fun getMatchById(matchId: String): Response<NetworkMatch> =
        networkApi.getMatchById(matchId)

    override suspend fun getPlayersByName(
        playerName: String,
        includeInactive: Int
    ): Response<List<NetworkPlayer>> =
        networkApi.getPlayersByName(playerName, includeInactive)

    override suspend fun getAllHeroes(): Response<List<NetworkHero>> =
        networkApi.getAllHeroes()

    override suspend fun getHeroByName(heroName: String): Response<NetworkHero> =
        networkApi.getHeroByName(heroName)

    override suspend fun getAllHeroStatistics(timeFrame: String?): Response<NetworkHeroStatisticsResponse> =
        networkApi.getAllHeroStatistics(timeFrame)

    override suspend fun getHeroStatisticsById(heroId: String): Response<NetworkHeroStatisticsResponse> =
        networkApi.getHeroStatisticsById("[$heroId]")

    override suspend fun getAllItems(): Response<List<NetworkItem>> =
        networkApi.getAllItems()

    override suspend fun getItemByName(itemName: String): Response<NetworkItem> =
        networkApi.getItemByName(itemName)

    override suspend fun getBuilds(
        name: String?,
        role: String?,
        order: String?,
        heroId: Long?,
        skillOrder: Int?,
        modules: Int?,
        currentVersion: Int?,
        page: Int?
    ): Response<List<NetworkHeroBuild>> =
        networkApi.getBuilds(
            name = name,
            role = role,
            order = order,
            heroId = heroId,
            skillOrder = skillOrder,
            modules = modules,
            currentVersion = currentVersion,
            page = page
        )

    override suspend fun getBuildById(buildId: String): Response<NetworkHeroBuild> =
        networkApi.getBuildById(buildId)

    override suspend fun deeplinkToNewBuild(
        title: String,
        description: String,
        role: String,
        heroId: Int,
        crestId: Int,
        itemIds: Map<String, Int>,
        skillOrder: Map<String, Int>,
        modules: Map<String, String>
    ) =
        networkApi.deeplinkToNewBuild(
            title = title,
            description = description,
            role = role,
            heroId = heroId,
            crestId = crestId,
            itemIds = itemIds,
            skillOrder = skillOrder,
            modules = modules,
        )
}