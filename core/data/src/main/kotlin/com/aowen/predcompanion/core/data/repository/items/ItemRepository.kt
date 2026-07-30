package com.aowen.predcompanion.core.data.repository.items


import com.aowen.predcompanion.core.model.data.ItemDetails
import kotlinx.coroutines.flow.StateFlow

interface ItemRepository {

    /**
     * Reactive view of the in-memory item cache keyed by item id. Empty until
     * [fetchAllItems] has completed successfully at least once.
     */
    val allItems: StateFlow<Map<String, ItemDetails>>

    /**
     * Populates the in-memory item caches from the network on first successful call.
     * Subsequent calls are a no-op for the lifetime of the process. Failures are
     * swallowed — callers observe "cache still empty" via [getAllItems] and friends.
     */
    suspend fun fetchAllItems()

    /** Returns the cached items, or an empty list if the cache is cold or the fetch failed. */
    fun getAllItems(): List<ItemDetails>

    /** Returns the cached item by its canonical `name`, or null if unknown / cache cold. */
    fun getItemByName(itemName: String): ItemDetails?

    /** Returns the cached item by its `displayName`, or null if unknown / cache cold. */
    fun getItemByDisplayName(displayName: String): ItemDetails?

    /** Returns the cached item by its numeric id, or null if unknown / cache cold. */
    fun getItemById(itemId: String): ItemDetails?

    fun getCrestById(itemId: String): ItemDetails?

    /** Returns the cached item image source by its numeric id, or null if unknown / cache cold. */
    fun getItemImageSrcById(itemId: String): String


    /** Returns cached items matching the given ids, preserving input order; unknown ids are skipped. */
    fun getItemsByIds(itemIds: List<String>): List<ItemDetails>

    /** Returns cached item image sources matching the given ids, preserving input order; unknown ids are skipped. */
    fun getItemImageSrcsByIds(itemIds: List<String>): List<String>
}
