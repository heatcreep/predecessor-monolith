package com.aowen.monolith.data.repository.items

import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.network.Resource

interface ItemRepository {
    suspend fun fetchAllItems(): Resource<List<ItemDetails>>
    suspend fun fetchItemByName(itemName: String): Resource<ItemDetails>
}