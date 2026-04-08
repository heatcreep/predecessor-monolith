package com.aowen.predcompanion.fakes.data

import com.aowen.predcompanion.data.Team
import com.aowen.predcompanion.data.asMatchDetails
import com.aowen.predcompanion.data.getDetailsWithItems

val fakeMatchDetails = fakeMatchDto.asMatchDetails()

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