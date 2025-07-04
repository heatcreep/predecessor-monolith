package com.aowen.monolith.network

import com.aowen.monolith.data.BuildDto
import com.aowen.monolith.data.HeroDto
import com.aowen.monolith.data.HeroStatisticsResponseDto
import com.aowen.monolith.data.ItemDto
import com.aowen.monolith.data.MatchDto
import com.aowen.monolith.data.MatchesDto
import com.aowen.monolith.data.PlayerDto
import com.aowen.monolith.data.PlayerHeroStatsResponseDto
import com.aowen.monolith.data.PlayerStatsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface OmedaCityService {
    // Player Details
    @GET("/players/{player_id}.json")
    suspend fun getPlayerById(@Path("player_id") playerId: String): Response<PlayerDto>

    @GET("/players/{player_id}/hero_statistics.json")
    suspend fun getPlayerHeroStatsById(@Path("player_id") playerId: String): Response<PlayerHeroStatsResponseDto>

    // Player Stats
    @GET("players/{player_id}/statistics.json")
    suspend fun getPlayerStatsById(@Path("player_id") playerId: String): Response<PlayerStatsDto>

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
    ): Response<MatchesDto>

    @GET("/matches/{match_id}.json")
    suspend fun getMatchById(@Path("match_id") matchId: String): Response<MatchDto>

    // Find Players By Name
    @GET("players.json")
    suspend fun getPlayersByName(
        @Query("filter[name]") playerName: String,
        @Query("filter[include_inactive]") includeInactive: Int = 1
    ): Response<List<PlayerDto>>

    @GET("heroes.json")
    suspend fun getAllHeroes(): Response<List<HeroDto>>

    @GET("heroes/{hero_name}.json")
    suspend fun getHeroByName(@Path("hero_name") heroName: String): Response<HeroDto>

    @GET("dashboard/hero_statistics.json")
    suspend fun getAllHeroStatistics(@Query("time_frame") timeFrame: String?): Response<HeroStatisticsResponseDto>

    @GET("dashboard/hero_statistics.json")
    suspend fun getHeroStatisticsById(@Query("hero_ids") heroIds: String): Response<HeroStatisticsResponseDto>

    @GET("items.json")
    suspend fun getAllItems(): Response<List<ItemDto>>

    @GET("items/{item_name}.json")
    suspend fun getItemByName(@Path("item_name") itemName: String): Response<ItemDto>


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
    ): Response<List<BuildDto>>

    @GET("builds/{build_id}.json")
    suspend fun getBuildById(
        @Path("build_id") buildId: String
    ): Response<BuildDto>

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