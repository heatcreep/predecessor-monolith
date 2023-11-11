package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.PlayerInfo
import com.aowen.monolith.data.create

val fakePlayerInfo = PlayerInfo(
    playerDetails = fakePlayerDto.create(),
    playerStats = fakePlayerStatsDto.create()
)