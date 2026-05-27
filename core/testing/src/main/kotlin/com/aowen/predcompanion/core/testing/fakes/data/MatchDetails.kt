package com.aowen.predcompanion.core.testing.fakes.data

import com.aowen.predcompanion.core.model.data.Team
import com.aowen.predcompanion.core.model.data.asMatchDetails
import com.aowen.predcompanion.core.model.data.getDetailsWithItems
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkMatch

val fakeMatchDetails = fakeNetworkMatch.asMatchDetails()

val fakeDuskTeam = Team.Dusk(fakeMatchDetails.dusk.players.map {
    it.getDetailsWithItems(fakeAllItems)
})

val fakeDawnTeam = Team.Dawn(fakeMatchDetails.dawn.players.map {
    it.getDetailsWithItems(fakeAllItems)
})

val fakeMatchDetailsWithItems = fakeMatchDetails.copy(
    dusk = fakeDuskTeam,
    dawn = fakeDawnTeam
)