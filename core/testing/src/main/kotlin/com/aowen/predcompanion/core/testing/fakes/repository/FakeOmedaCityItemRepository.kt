package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.model.data.asItemDetails
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem2
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem3
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem4
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeOmedaCityItemRepository :
    ItemRepository {

    companion object {
        val FAKE_ITEM_LIST = listOf(
            fakeNetworkItem.asItemDetails(),
            fakeNetworkItem2.asItemDetails(),
            fakeNetworkItem3.asItemDetails(),
            fakeNetworkItem4.asItemDetails()
        )
    }

    override val allItems: StateFlow<Map<Int, ItemDetails>> = MutableStateFlow(FAKE_ITEM_LIST.associateBy { it.id })

    override suspend fun fetchAllItems() = Unit

    override fun getAllItems(): List<ItemDetails> = FAKE_ITEM_LIST

    override fun getItemByName(itemName: String): ItemDetails? = FAKE_ITEM_LIST.find { it.name == itemName }

    override fun getItemByDisplayName(displayName: String): ItemDetails? = FAKE_ITEM_LIST.find { it.displayName == displayName }

    override fun getItemById(itemId: Int): ItemDetails? = FAKE_ITEM_LIST.find { it.id == itemId }

    override fun getItemImageSrcById(itemId: Int): String = FAKE_ITEM_LIST.find { it.id == itemId }?.imageSrc ?: ""

    override fun getItemsByIds(itemIds: List<Int>): List<ItemDetails> = FAKE_ITEM_LIST.filter { itemIds.contains(it.id) }

    override fun getItemImageSrcsByIds(itemIds: List<Int>): List<String> = FAKE_ITEM_LIST.filter { itemIds.contains(it.id) }
        .map { it.imageSrc }
}