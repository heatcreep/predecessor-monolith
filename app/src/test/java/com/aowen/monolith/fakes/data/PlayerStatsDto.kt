package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.PlayerStatsDto

val fakePlayerStatsDto = PlayerStatsDto(
    matchesPlayed = 123,
    winRate = 0.56f,
    averageKda = listOf(
        1.2.toBigDecimal(),
        3.4.toBigDecimal(),
        5.6f.toBigDecimal()
    ),
    averageKdaRatio = 1.23.toBigDecimal(),
    averagePerformanceScore = 123.45.toBigDecimal(),
    favoriteHero = fakeHeroDto,
    favoriteRole = "favoriteRole",
    hoursPlayed = 240.0.toBigDecimal()
)