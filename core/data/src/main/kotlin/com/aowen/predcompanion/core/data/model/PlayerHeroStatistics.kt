package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.model.data.PlayerHeroStatistics
import com.aowen.predcompanion.core.network.apollo.fragment.PlayerFragment

fun PlayerFragment.HeroStatistics.asPlayerHeroStatistics() =
    this.results.map { result ->
        PlayerHeroStatistics(
            heroId = result.hero.heroFragment.id,
            heroName = result.hero.heroFragment.data?.displayName ?: "",
            matchCount = result.matchesPlayed,
            matchesWon = result.matchesWon,
            totalKills = result.totalKills,
            totalDeaths = result.totalDeaths,
            totalAssists = result.totalAssists
        )
    }.sortedByDescending { it.matchCount }