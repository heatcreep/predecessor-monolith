package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.asItemDetails


val fakeAllItems = listOf(
    fakeItemDto.asItemDetails(),
    fakeItemDto2.asItemDetails(),
    fakeItemDto3.asItemDetails(),
    fakeItemDto4.asItemDetails()
)