package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.Team
import com.aowen.monolith.data.create
import com.aowen.monolith.data.getDetailsWithItems

val fakeMatchDetails = fakeMatchDto.create()

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