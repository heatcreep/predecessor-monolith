package com.aowen.monolith.data

data class PlayerStats(
    val matchesPlayed: String = "",
    val hoursPlayed: String = "",
    val averagePerformanceScore: String = "",
    val averageKda: List<String> = emptyList(),
    val averageKdaRatio: String = "",
    val favoriteHero: String = "",
    val favoriteRole: String = "",
    val winRate: String = ""

)

fun PlayerStatsDto.create(): PlayerStats {
    return PlayerStats(
        matchesPlayed = this.matchesPlayed.toString(),
        hoursPlayed = this.hoursPlayed.toString(),
        averagePerformanceScore = this.averagePerformanceScore.toString(),
        averageKda = this.averageKda.map { it.toString() },
        averageKdaRatio = this.averageKdaRatio.toString(),
        favoriteHero = this.favoriteHero.name,
        favoriteRole = this.favoriteRole?.replaceFirstChar { it.uppercase() } ?: "None",
        winRate = this.winRate.toPercentageString()
    )
}