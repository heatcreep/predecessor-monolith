package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.model.data.ItemDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeItemRepository : ItemRepository {

    private val _allItems = MutableStateFlow<Map<String, ItemDetails>>(emptyMap())
    override val allItems: StateFlow<Map<String, ItemDetails>> = _allItems

    var itemsToReturn: List<ItemDetails> = emptyList()

    override suspend fun fetchAllItems() {
        _allItems.value = itemsToReturn.associateBy { it.id }
    }

    override fun getAllItems(): List<ItemDetails> = _allItems.value.values.toList()

    override fun getItemByName(itemName: String): ItemDetails? =
        _allItems.value.values.firstOrNull { it.name == itemName }

    override fun getItemByDisplayName(displayName: String): ItemDetails? =
        _allItems.value.values.firstOrNull { it.displayName == displayName }

    override fun getItemById(itemId: String): ItemDetails? = _allItems.value[itemId]

    override fun getCrestById(itemId: String): ItemDetails? = _allItems.value[itemId]

    override fun getItemImageSrcById(itemId: String): String =
        _allItems.value[itemId]?.imageSrc ?: ""

    override fun getItemsByIds(itemIds: List<String>): List<ItemDetails> =
        itemIds.mapNotNull { _allItems.value[it] }

    override fun getItemImageSrcsByIds(itemIds: List<String>): List<String> =
        itemIds.mapNotNull { _allItems.value[it]?.imageSrc }

    fun emitItems(items: List<ItemDetails>) {
        _allItems.value = items.associateBy { it.id }
    }
}
