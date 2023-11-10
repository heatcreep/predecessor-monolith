package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.PlayerDto

val fakePlayerDto =  PlayerDto(
    id = "123",
    rank = 104,
    rankTitle = "Gold III",
    displayName = "RealGamer",
    isRanked = true,
    mmr = 1234.56f,
    rankImage = "test",
    region = "naeast",
    flags = emptyList()
)