package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.HeroStatisticsDto

val fakeHeroStatisticsDto = HeroStatisticsDto(
    heroId = "hero1",
    displayName = "Hero 1",
    matchCount = 10,
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