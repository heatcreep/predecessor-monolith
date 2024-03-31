package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.HeroStatistics
import com.aowen.monolith.data.HeroStatisticsDto

val fakeHeroStatisticsDto = HeroStatisticsDto(
    heroId = 1,
    displayName = "Hero 1",
    matchCount = 10f,
    winRate = 0.5f,
    pickRate = 0.7f,
    kills = 20,
    deaths = 10,
    assists = 30,
    averageKdar = 2.0f,
    averageCs = 100.0f,
    averageGold = 5000.0f,
    averagePerformanceScore = 100.0f,
    averageDamageDealtToHeroes = 2000.0f,
    averageDamageTakenFromHeroes = 1000.0f,
    averageGameDuration = 1800.0f
)

val fakeHeroStatisticsResult = listOf(
    HeroStatistics(
        winRate = 70f,
        pickRate = 40f,
    ),
    HeroStatistics(
        winRate = 60f,
        pickRate = 50f,
    ),
    HeroStatistics(
        winRate = 50f,
        pickRate = 60f,
    ),
    HeroStatistics(
        winRate = 40f,
        pickRate = 70f,
    ),
    HeroStatistics(
        winRate = 30f,
        pickRate = 80f,
    ),
    HeroStatistics(
        winRate = 20f,
        pickRate = 90f,
    ),
)

val fakeTopFiveHeroPickRateSorted = listOf(
    HeroStatistics(
        winRate = 20f,
        pickRate = 90f,
    ),
    HeroStatistics(
        winRate = 30f,
        pickRate = 80f,
    ),
    HeroStatistics(
        winRate = 40f,
        pickRate = 70f,
    ),
    HeroStatistics(
        winRate = 50f,
        pickRate = 60f,
    ),
    HeroStatistics(
        winRate = 60f,
        pickRate = 50f,
    ),
    HeroStatistics(
        winRate = 70f,
        pickRate = 40f,
    ),
)