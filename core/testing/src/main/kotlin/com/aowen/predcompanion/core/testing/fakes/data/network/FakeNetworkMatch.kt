package com.aowen.predcompanion.core.testing.fakes.data.network

import com.aowen.predcompanion.core.network.model.NetworkMatch
import com.aowen.predcompanion.core.network.model.NetworkMatchPlayer
import com.aowen.predcompanion.core.network.model.NetworkMatchTeam

val fakeNetworkMatchPlayer = NetworkMatchPlayer(
    id = "player1",
    displayName = "Player 1",
    heroId = 1,
    role = "role1",
    performanceScore = 100.0,
    performanceTitle = "title1",
    kills = 1.0f,
    deaths = 2.0f,
    assists = 3.0f,
    minionsKilled = 4,
    goldEarned = 5,
    inventoryData = listOf(1, 2, 3, 4, 5, 6),
    crestHealingDone = 100,
    goldSpent = 100,
    itemHealingDone = 100,
    laneMinionsKilled = 100,
    largestCriticalStrike = 100,
    largestMultiKill = 100,
    largestKillingSpree = 100,
    magicalDamageDealt = 100,
    magicalDamageDealtToHeroes = 100,
    magicalDamageTaken = 100,
    magicalDamageTakenFromHeroes = 100,
    neutralMinionsEnemyJungle = 100,
    neutralMinionsKilled = 100,
    neutralMinionsTeamJungle = 100,
    physicalDamageDealt = 100,
    physicalDamageDealtToHeroes = 100,
    physicalDamageTaken = 100,
    physicalDamageTakenFromHeroes = 100,
    totalDamageDealt = 100,
    totalDamageDealtToHeroes = 100,
    totalDamageDealtToObjectives = 100,
    totalDamageDealtToStructures = 100,
    totalDamageMitigated = 100,
    totalDamageTaken = 100,
    totalHealingDone = 100,
    totalDamageTakenFromHeroes = 100,
    trueDamageDealt = 100,
    totalShieldingReceived = 100,
    trueDamageDealtToHeroes = 100,
    trueDamageTaken = 100,
    wardsDestroyed = 100,
    wardsPlaced = 100,
    trueDamageTakenFromHeroes = 100,
    utilityHealingDone = 100,
    team = "dawn",
    vpTotal = 100,
    vpChange = 100,
    rank = 100,


    )

val fakeNetworkDawnTeam = NetworkMatchTeam(
    averageMmr = "1000",
    players = listOf(
        fakeNetworkMatchPlayer,
        fakeNetworkMatchPlayer,
        fakeNetworkMatchPlayer,
        fakeNetworkMatchPlayer,
        fakeNetworkMatchPlayer
    )
)

val fakeNetworkDuskTeam = NetworkMatchTeam(
    averageMmr = "1000",
    players = listOf(
        fakeNetworkMatchPlayer,
        fakeNetworkMatchPlayer,
        fakeNetworkMatchPlayer,
        fakeNetworkMatchPlayer,
        fakeNetworkMatchPlayer
    )
)

val fakeNetworkMatch = NetworkMatch(
    id = "validMatchId",
    startTime = "2022-01-01T00:00:00Z",
    endTime = "2022-01-01T01:00:00Z",
    gameDuration = 3600,
    region = "NA",
    winningTeam = "dawn",
    gameMode = "pvp",
    players = listOf(
        fakeNetworkMatchPlayer,
    )
)

