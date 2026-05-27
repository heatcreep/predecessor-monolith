package com.aowen.predcompanion.core.model.data

import kotlinx.serialization.Serializable

@Serializable
data class PlayerInfo(
    val playerDetails: PlayerDetails,
    val playerStats: PlayerStats
) {
    @Serializable
    data class PlayerDetails(
        val playerId: String = "",
        val playerName: String = "",
        val region: String? = null,
        val rank: Int = 0,
        val rankTitle: String = "",
        val rankImage: String = "",
        val isRanked: Boolean = false,
        val vpTotal: Int = 0,
        val vpCurrent: Int = 0,
        val mmr: String? = null,
        val isMmrDisabled: Boolean = false,
        val isCheater: Boolean = false,
    ) {
        val isConsolePlayer = playerName.contains("\uD83C\uDFAE user-")
    }

    @Serializable
    data class PlayerStats(
        val matchesPlayed: String = "",
        val hoursPlayed: String = "",
        val averagePerformanceScore: String = "",
        val averageKda: List<String> = emptyList(),
        val averageKdaRatio: String = "",
        val favoriteHero: FavoriteHero? = null,
        val favoriteRole: String = "",
        val winRate: String = ""

    ) {
        @Serializable
        data class FavoriteHero(
            val name: String = "",
            val imageUrl: String? = null,
        )
    }
}
