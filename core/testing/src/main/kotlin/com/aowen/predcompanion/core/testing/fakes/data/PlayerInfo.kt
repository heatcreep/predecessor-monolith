package com.aowen.predcompanion.core.testing.fakes.data

import com.aowen.predcompanion.core.model.data.PlayerInfo
import com.aowen.predcompanion.core.model.data.asPlayerDetails
import com.aowen.predcompanion.core.model.data.create
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkPlayer
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkPlayerStats

val fakePlayerInfo = PlayerInfo(
    playerDetails = fakeNetworkPlayer.asPlayerDetails(),
    playerStats = fakeNetworkPlayerStats.create()
)