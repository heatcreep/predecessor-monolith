package com.aowen.predcompanion.core.data.repository.items

import com.aowen.predcompanion.core.data.model.asItemDetails
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeGraphQlCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OmedaCityItemRepository @Inject constructor(
    private val predGGNetwork: PredGGNetworkDataSource,
) : ItemRepository {

    private val _allItems: MutableStateFlow<Map<Int, ItemDetails>> = MutableStateFlow(emptyMap())
    override val allItems: StateFlow<Map<Int, ItemDetails>> = _allItems

    override suspend fun fetchAllItems() {
        val result = safeGraphQlCall(
            apiCall = predGGNetwork::getAllItems,
            transform = { data -> data.items.mapNotNull { it.itemFragment.data?.asItemDetails() } }
        )
        if (result is Resource.Success) {
            _allItems.update {
                result.data
                    .filter { it.id.isNotBlank() }
                    .associateBy { it.id.toInt() }
            }
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


