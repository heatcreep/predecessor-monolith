package com.aowen.monolith.data

data class HeroStatistics(
    val heroId: Int = 0,
    val name: String = "",
    val winRate: Float = 0f,
    val pickRate: Float = 0f,
)

fun HeroStatisticsDto.create(): HeroStatistics {
    return HeroStatistics(
        heroId = this.heroId,
        name = this.displayName,
        winRate = this.winRate,
        pickRate = this.pickRate,
    )
}
