package com.aowen.predcompanion.core.data.repository

import com.aowen.predcompanion.core.model.data.asItemDetails
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.data.repository.items.OmedaCityItemRepository
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem2
import com.aowen.predcompanion.core.testing.fakes.service.TestPredCompanionNetworkDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ItemRepositoryTest {

    companion object {
        private const val ERROR_MESSAGE_UNKNOWN = "Something went wrong"
        private const val ERROR_MESSAGE_404 = "Failed to fetch data. (Code: 404 - Not Found)"
    }

    private lateinit var itemRepository: ItemRepository

    @Before
    fun setUp() {
        itemRepository = OmedaCityItemRepository(
            networkDataSource = TestPredCompanionNetworkDataSource(200)
        )
    }

    // fetchItemByName
    @Test
    fun `fetchItemByName - successful response returns an ItemDetails`() = runTest {
        itemRepository.fetchAllItems()
        val actual = itemRepository.getItemByName("Item A")
        val expected = fakeNetworkItem2.asItemDetails()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `fetchItemByName - non-successful response returns exception with message`() = runTest {
        itemRepository = OmedaCityItemRepository(
            networkDataSource = TestPredCompanionNetworkDataSource(404)
        )
        val actual = itemRepository.getItemByName("test")
        Assert.assertTrue(actual == null)
    }

    @Test
    fun `fetchItemByName - thrown exception returns failure with message`() = runTest {
        itemRepository = OmedaCityItemRepository(
            networkDataSource = TestPredCompanionNetworkDataSource()
        )
        val actual = itemRepository.getItemByName("test")
        Assert.assertTrue(actual == null)
    }

}