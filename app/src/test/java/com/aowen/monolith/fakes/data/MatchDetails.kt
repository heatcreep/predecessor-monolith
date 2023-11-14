package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.create
import com.aowen.monolith.data.getDetailsWithItems

val fakeMatchDetails = fakeMatchDto.create()

val fakeMatchDetailsWithItems = fakeMatchDetails.copy(
    dusk = fakeMatchDetails.dusk.copy(
        players = fakeMatchDetails.dusk.players.map { player ->
            player.getDetailsWithItems(fakeAllItems)
        }
    ),
    dawn = fakeMatchDetails.dawn.copy(
        players = fakeMatchDetails.dawn.players.map { player ->
            player.getDetailsWithItems(fakeAllItems)
        }
    )
)