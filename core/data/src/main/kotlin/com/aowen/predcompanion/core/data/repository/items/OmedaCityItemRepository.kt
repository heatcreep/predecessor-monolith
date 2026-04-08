package com.aowen.predcompanion.core.data.repository.items

import com.aowen.predcompanion.data.ItemDetails
import com.aowen.predcompanion.data.asItemDetails
import com.aowen.predcompanion.core.network.OmedaCityService
import com.aowen.predcompanion.core.network.Resource
import com.aowen.predcompanion.core.network.safeApiCall
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