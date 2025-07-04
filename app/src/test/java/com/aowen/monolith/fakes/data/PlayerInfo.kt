package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.PlayerInfo
import com.aowen.monolith.data.create
import com.aowen.monolith.data.asPlayerDetails

val fakePlayerInfo = PlayerInfo(
    playerDetails = fakePlayerDto.asPlayerDetails(),
    playerStats = fakePlayerStatsDto.create()
)