package com.aowen.predcompanion.core.testing.fakes.data.network

import com.aowen.predcompanion.core.network.model.NetworkPlayerStats

val fakeNetworkPlayerStats = NetworkPlayerStats(
    matchesPlayed = 123,
    winRate = 0.56f,
    averageKda = listOf(
        1.2.toBigDecimal(),
        3.4.toBigDecimal(),
        5.6f.toBigDecimal()
    ),
    averageKdaRatio = 1.23.toBigDecimal(),
    averagePerformanceScore = 123.45.toBigDecimal(),
    favoriteHero = fakeNetworkFavoriteHero,
    favoriteRole = "favoriteRole",
    hoursPlayed = 240.0.toBigDecimal()
)