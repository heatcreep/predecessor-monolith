package com.aowen.predcompanion.core.data.repository.items

import com.aowen.predcompanion.core.data.model.asItemDetails
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.network.PredCompanionNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeApiCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OmedaCityItemRepository @Inject constructor(
    private val networkDataSource: PredCompanionNetworkDataSource
) : ItemRepository {

    private val _allItems: MutableStateFlow<Map<Int, ItemDetails>> = MutableStateFlow(emptyMap())
    override val allItems: StateFlow<Map<Int, ItemDetails>> = _allItems

    override suspend fun fetchAllItems() {
        if (_allItems.value.isNotEmpty()) return
        val result = safeApiCall(
            apiCall = networkDataSource::getAllItems,
            transform = { items -> items.map { it.asItemDetails() } }
        )
        if (result is Resource.Success) {
            _allItems.update { result.data.associateBy { it.id } }
        }
    }

    override fun getAllItems(): List<ItemDetails> = _allItems.value.values.toList()

    override fun getItemByName(itemName: String): ItemDetails? =
        _allItems.value.values.firstOrNull { it.name == itemName }

    override fun getItemByDisplayName(displayName: String): ItemDetails? =
        _allItems.value.values.firstOrNull { it.displayName == displayName }

    override fun getItemById(itemId: Int): ItemDetails? = allItems.value[itemId]

    override fun getItemImageSrcById(itemId: Int): String = allItems.value[itemId]?.imageSrc ?: ""

    override fun getItemsByIds(itemIds: List<Int>): List<ItemDetails> =
        itemIds.mapNotNull { allItems.value[it] }

    override fun getItemImageSrcsByIds(itemIds: List<Int>): List<String> =
        itemIds.mapNotNull { allItems.value[it]?.imageSrc }

}


