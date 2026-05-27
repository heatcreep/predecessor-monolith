package com.aowen.predcompanion.core.testing.fakes.data.network

import com.aowen.predcompanion.core.model.data.ClaimedPlayer
import com.aowen.predcompanion.core.testing.fakes.data.fakePlayerInfo

val fakeClaimedPlayer = ClaimedPlayer(
    playerStats = fakePlayerInfo.playerStats,
    playerDetails = fakePlayerInfo.playerDetails
)