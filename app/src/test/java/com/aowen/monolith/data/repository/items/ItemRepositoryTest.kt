package com.aowen.monolith.data.repository.items

import com.aowen.monolith.data.asItemDetails
import com.aowen.monolith.fakes.FakeOmedaCityService
import com.aowen.monolith.fakes.data.fakeItemDto
import com.aowen.monolith.network.Resource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
            omedaCityService = FakeOmedaCityService(200)
        )
    }

    @Test
    fun `fetchAllItems - successful response returns a list of ItemDetails`() = runTest {
        val actual = itemRepository.fetchAllItems()
        val expected = listOf(
            fakeItemDto.asItemDetails()
        )
        assertEquals(Resource.Success(expected), actual)
    }

    @Test
    fun `fetchAllItems - non-successful response returns exception with message`() = runTest {
        itemRepository = OmedaCityItemRepository(
            omedaCityService = FakeOmedaCityService(404)
        )
        val actual = itemRepository.fetchAllItems()
        val expected = ERROR_MESSAGE_404
        assertTrue(actual is Resource.NetworkError)
        assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @Test
    fun `fetchAllItems - thrown exception returns failure with message`() = runTest {
        itemRepository = OmedaCityItemRepository(
            omedaCityService = FakeOmedaCityService()
        )
        val actual = itemRepository.fetchAllItems()
        val expected = ERROR_MESSAGE_UNKNOWN
        assertTrue(actual is Resource.GenericError)
        assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }

    // fetchItemByName

    @Test
    fun `fetchItemByName - successful response returns an ItemDetails`() = runTest {
        val actual = itemRepository.fetchItemByName("test")
        val expected = fakeItemDto.asItemDetails()
        assertEquals(Resource.Success(expected), actual)
    }

    @Test
    fun `fetchItemByName - non-successful response returns exception with message`() = runTest {
        itemRepository = OmedaCityItemRepository(
            omedaCityService = FakeOmedaCityService(404)
        )
        val actual = itemRepository.fetchItemByName("test")
        val expected = ERROR_MESSAGE_404
        assertTrue(actual is Resource.NetworkError)
        assertEquals(expected, (actual as Resource.NetworkError).errorMessage)
    }

    @Test
    fun `fetchItemByName - thrown exception returns failure with message`() = runTest {
        itemRepository = OmedaCityItemRepository(
            omedaCityService = FakeOmedaCityService()
        )
        val actual = itemRepository.fetchItemByName("test")
        val expected = ERROR_MESSAGE_UNKNOWN
        assertTrue(actual is Resource.GenericError)
        assertEquals(expected, (actual as Resource.GenericError).errorMessage)
    }

}