package com.aowen.predcompanion.core.testing.fakes.data

import com.aowen.predcompanion.core.model.data.asItemDetails
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem2
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem3
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem4


val fakeAllItems = listOf(
    fakeNetworkItem.asItemDetails(),
    fakeNetworkItem2.asItemDetails(),
    fakeNetworkItem3.asItemDetails(),
    fakeNetworkItem4.asItemDetails()
)