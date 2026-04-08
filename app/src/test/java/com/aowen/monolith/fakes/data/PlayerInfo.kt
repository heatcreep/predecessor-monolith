package com.aowen.predcompanion.fakes.data

import com.aowen.predcompanion.data.PlayerInfo
import com.aowen.predcompanion.core.model.data.create
import com.aowen.predcompanion.data.asPlayerDetails

val fakePlayerInfo = PlayerInfo(
    playerDetails = fakePlayerDto.asPlayerDetails(),
    playerStats = fakePlayerStatsDto.create()
)