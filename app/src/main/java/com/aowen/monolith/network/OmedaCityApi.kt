package com.aowen.monolith.network

import  com.aowen.monolith.data.HeroDto
import com.aowen.monolith.data.ItemDto
import com.aowen.monolith.data.MatchDto
import com.aowen.monolith.data.MatchesDto
import com.aowen.monolith.data.PlayerDto
import com.aowen.monolith.data.PlayerStatsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OmedaCityApi {
    // Player Details
    @GET("/players/{player_id}.json")
    suspend fun getPlayerById(@Path("player_id") playerId: String) : PlayerDto

    // Player Stats
    @GET("players/{player_id}/statistics.json")
    suspend fun getPlayerStatsById(@Path("player_id") playerId: String) : PlayerStatsDto

    // Player Matches
    @GET("/players/{player_id}/matches.json")
    suspend fun getPlayerMatchesById(@Path("player_id") playerId: String) : MatchesDto

    @GET("/matches/{match_id}.json")
    suspend fun getMatchById(@Path("match_id") matchId: String) : MatchDto

    // Find Players By Name
    @GET("players.json")
    suspend fun getPlayersByName(@Query("q[name]") playerName: String) : List<PlayerDto>

    @GET("heroes.json")
    suspend fun getAllHeroes() : List<HeroDto>

    @GET("items.json")
    suspend fun getAllItems(): List<ItemDto>
}