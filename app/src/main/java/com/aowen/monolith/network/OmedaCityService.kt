package com.aowen.monolith.network

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
    suspend fun getPlayerMatchesById(@Path("player_id") playerId: String): Response<MatchesDto>

    @GET("/matches/{match_id}.json")
    suspend fun getMatchById(@Path("match_id") matchId: String): Response<MatchDto>

    // Find Players By Name
    @GET("players.json")
    suspend fun getPlayersByName(@Query("q[name]") playerName: String): Response<List<PlayerDto>>

    @GET("heroes.json")
    suspend fun getAllHeroes(): Response<List<HeroDto>>

    @GET("heroes/{hero_name}.json")
    suspend fun getHeroByName(@Path("hero_name") heroName: String): Response<HeroDto>

    @GET("dashboard/hero_statistics.json")
    suspend fun getHeroStatisticsById(@Query("hero_ids") heroIds: String): Response<HeroStatisticsResponseDto>

    @GET("items.json")
    suspend fun getAllItems(): Response<List<ItemDto>>
}