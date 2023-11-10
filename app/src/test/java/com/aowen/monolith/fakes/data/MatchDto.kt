package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.MatchDto
import com.aowen.monolith.data.MatchPlayerDto
import com.aowen.monolith.data.TeamDto

val fakeMatchPlayerDto = MatchPlayerDto(
    id = "player1",
    displayName = "Player 1",
    mmr = 1000,
    mmrChange = 100.0f,
    rankImage = "test",
    hero = "hero1",
    role = "role1",
    performanceScore = 100.0,
    performanceTitle = "title1",
    kills = 1.0f,
    deaths = 2.0f,
    assists = 3.0f,
    minionsKilled = 4,
    goldEarned = 5,
    inventoryData = listOf(1, 2, 3, 4, 5, 6)
)

val fakeDawnTeam = TeamDto(
averageMmr = "1000",
    players = listOf(
        fakeMatchPlayerDto,
        fakeMatchPlayerDto,
        fakeMatchPlayerDto,
        fakeMatchPlayerDto,
        fakeMatchPlayerDto
    )
)

val fakeDuskTeam = TeamDto(
averageMmr = "1000",
    players = listOf(
        fakeMatchPlayerDto,
        fakeMatchPlayerDto,
        fakeMatchPlayerDto,
        fakeMatchPlayerDto,
        fakeMatchPlayerDto
    )
)

val fakeMatchDto = MatchDto(
    id = "match1",
    startTime = "2022-01-01T00:00:00Z",
    endTime = "2022-01-01T01:00:00Z",
    gameDuration = 3600,
    region = "NA",
    winningTeam = "dawn",
    dawn = fakeDawnTeam,
    dusk = fakeDuskTeam
)

