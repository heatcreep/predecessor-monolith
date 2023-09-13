package com.aowen.monolith.data

data class HeroStatistics(
    val winRate: Float = 0f,
    val pickRate: Float = 0f,
)

fun HeroStatisticsDto.create(): HeroStatistics {
    return HeroStatistics(
        winRate = this.winRate,
        pickRate = this.pickRate,
    )
}
