package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.network.model.NetworkPlayerHeroStats
import com.aowen.predcompanion.data.PlayerHeroStats
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun NetworkPlayerHeroStats.asPlayerHeroStats(): PlayerHeroStats {

    val symbols = DecimalFormatSymbols(Locale.US)
    val df = DecimalFormat("#.##", symbols)


    return PlayerHeroStats(
        heroId = heroId,
        displayName = displayName,
        matchCount = matchCount,
        winRate = df.format(winRate?.let { it * 100 } ?: 0).toDouble(),
        csMin = csMin,
        goldMin = goldMin,
        largestKillingSpree = largestKillingSpree,
        largestMultiKill = largestMultiKill,
        largestCriticalStrike = largestCriticalStrike,
        totalPerformanceScore = totalPerformanceScore,
        avgPerformanceScore = avgPerformanceScore,
        maxPerformanceScore = maxPerformanceScore,
        kills = kills,
        avgKills = avgKills,
        maxKills = maxKills,
        deaths = deaths,
        avgDeaths = avgDeaths,
        maxDeaths = maxDeaths,
        assists = assists,
        avgAssists = avgAssists,
        maxAssists = maxAssists,
        avgKdar = avgKdar,
        maxKdar = maxKdar,
        minionsKilled = minionsKilled,
        avgMinionsKilled = avgMinionsKilled,
        maxMinionsKilled = maxMinionsKilled,
        goldEarned = goldEarned,
        avgGoldEarned = avgGoldEarned,
        maxGoldEarned = maxGoldEarned,
        totalHealingDone = totalHealingDone,
        avgHealingDone = avgHealingDone,
        maxHealingDone = maxHealingDone,
        totalDamageMitigated = totalDamageMitigated,
        avgDamageMitigated = avgDamageMitigated,
        maxDamageMitigated = maxDamageMitigated,
        totalDamageDealtToHeroes = totalDamageDealtToHeroes,
        avgDamageDealtToHeroes = avgDamageDealtToHeroes,
        maxDamageDealtToHeroes = maxDamageDealtToHeroes,
        totalDamageTakenFromHeroes = totalDamageTakenFromHeroes,
        avgDamageTakenFromHeroes = avgDamageTakenFromHeroes,
        maxDamageTakenFromHeroes = maxDamageTakenFromHeroes,
        totalDamageDealtToStructures = totalDamageDealtToStructures,
        avgDamageDealtToStructures = avgDamageDealtToStructures,
        maxDamageDealtToStructures = maxDamageDealtToStructures,
        totalDamageDealtToObjectives = totalDamageDealtToObjectives,
        avgDamageDealtToObjectives = avgDamageDealtToObjectives,
        maxDamageDealtToObjectives = maxDamageDealtToObjectives,
        wardsPlaced = wardsPlaced,
        avgWardsPlaced = avgWardsPlaced,
        maxWardsPlaced = maxWardsPlaced,
        wardsDestroyed = wardsDestroyed,
        avgWardsDestroyed = avgWardsDestroyed,
        maxWardsDestroyed = maxWardsDestroyed
    )
}