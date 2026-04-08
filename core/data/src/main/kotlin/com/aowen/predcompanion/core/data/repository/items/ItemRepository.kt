package com.aowen.predcompanion.core.data.repository.items

import com.aowen.predcompanion.data.ItemDetails
import com.aowen.predcompanion.core.network.Resource

interface ItemRepository {
    suspend fun fetchAllItems(): Resource<List<ItemDetails>>
    suspend fun fetchItemByName(itemName: String): Resource<ItemDetails>
}