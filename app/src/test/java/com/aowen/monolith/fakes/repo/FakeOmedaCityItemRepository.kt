package com.aowen.monolith.fakes.repo

import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.asItemDetails
import com.aowen.monolith.data.repository.items.ItemRepository
import com.aowen.monolith.fakes.data.fakeItemDto
import com.aowen.monolith.fakes.data.fakeItemDto2
import com.aowen.monolith.fakes.data.fakeItemDto3
import com.aowen.monolith.fakes.data.fakeItemDto4
import com.aowen.monolith.network.Resource

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