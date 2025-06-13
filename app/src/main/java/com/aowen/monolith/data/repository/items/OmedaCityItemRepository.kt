package com.aowen.monolith.data.repository.items

import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.asItemDetails
import com.aowen.monolith.network.OmedaCityService
import com.aowen.monolith.network.Resource
import com.aowen.monolith.network.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OmedaCityItemRepository @Inject constructor(
    private val omedaCityService: OmedaCityService
) : ItemRepository {
    override suspend fun fetchAllItems(): Resource<List<ItemDetails>> =
        safeApiCall(
            apiCall = omedaCityService::getAllItems,
            transform = { items -> items.map { it.asItemDetails() } }
        )

    override suspend fun fetchItemByName(itemName: String): Resource<ItemDetails> =
        safeApiCall(
            apiCall = { omedaCityService.getItemByName(itemName) },
            transform = { item -> item.asItemDetails() }
        )
}