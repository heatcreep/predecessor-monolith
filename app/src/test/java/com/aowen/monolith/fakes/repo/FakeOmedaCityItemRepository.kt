package com.aowen.predcompanion.fakes.repo

import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.model.data.asItemDetails
import com.aowen.predcompanion.data.repository.items.ItemRepository
import com.aowen.monolith.fakes.data.fakeItemDto
import com.aowen.predcompanion.fakes.data.fakeItemDto2
import com.aowen.predcompanion.fakes.data.fakeItemDto3
import com.aowen.predcompanion.fakes.data.fakeItemDto4
import com.aowen.predcompanion.core.network.Resource

class FakeOmedaCityItemRepository : ItemRepository {

    companion object {
        val FAKE_ITEM_LIST = listOf(
            fakeItemDto.asItemDetails(),
            fakeItemDto2.asItemDetails(),
            fakeItemDto3.asItemDetails(),
            fakeItemDto4.asItemDetails()
        )
    }
    override suspend fun fetchAllItems(): Resource<List<ItemDetails>> =
        Resource.Success(
            FAKE_ITEM_LIST
        )


    override suspend fun fetchItemByName(itemName: String): Resource<ItemDetails> =
        Resource.Success(
            fakeItemDto.asItemDetails()
        )
}