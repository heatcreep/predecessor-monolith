package com.aowen.monolith.data

data class HeroStatistics(
    val heroId: Long = 0,
    val name: String = "",
    val heroName: String = "",
    val winRate: Float = 0f,
    val pickRate: Float = 0f,
)

fun HeroStatisticsDto.create(): HeroStatistics {
    return HeroStatistics(
        heroId = this.heroId,
        heroName = this.displayName,
        name = Hero.entries.find { it.heroName == this.displayName }?.pathName ?: "",
        winRate = this.winRate,
        pickRate = this.pickRate,
    )
}
