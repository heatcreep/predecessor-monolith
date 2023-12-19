package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.create
import com.aowen.monolith.data.getDetailsWithItems

val fakeMatchDetails = fakeMatchDto.create()

val fakeMatchDetailsWithItems = fakeMatchDetails.copy(
    dusk = fakeMatchDetails.dusk.map {
        it.getDetailsWithItems(fakeAllItems)
    },
    dawn = fakeMatchDetails.dawn.map {
        it.getDetailsWithItems(fakeAllItems)
    }
)