@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.monolith.ui

import com.aowen.monolith.data.asItemDetails
import com.aowen.monolith.fakes.data.fakeItemDto
import com.aowen.monolith.fakes.data.fakeItemDto2
import com.aowen.monolith.fakes.data.fakeItemDto3
import com.aowen.monolith.fakes.data.fakeItemDto4
import com.aowen.monolith.fakes.repo.FakeOmedaCityItemRepository
import com.aowen.monolith.feature.items.ItemsUiState
import com.aowen.monolith.feature.items.ItemsViewModel
import com.aowen.monolith.network.Resource
import com.aowen.monolith.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ItemsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private var itemRepository = FakeOmedaCityItemRepository()

    private lateinit var viewModel: ItemsViewModel



    @Before
    fun setUp() {
        viewModel = ItemsViewModel(
            itemRepository = itemRepository
        )
    }

    @Test
    fun `initViewModel() should set uiState to ItemsUiState`() = runTest {

        // Then
        val expected = ItemsUiState(
            isLoading = false,
            allItems = listOf(
                fakeItemDto2.asItemDetails(),
                fakeItemDto.asItemDetails(),
                fakeItemDto3.asItemDetails(),
                fakeItemDto4.asItemDetails()
            ),
            filteredItems = listOf(
                fakeItemDto2.asItemDetails(),
                fakeItemDto3.asItemDetails(),
                fakeItemDto4.asItemDetails(),
            ),
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set uiState to ItemsUiState with error`() = runTest {
        itemRepository = mockk()
        coEvery { itemRepository.fetchAllItems() } returns Resource.NetworkError(404, "Failed to fetch items")
        viewModel = ItemsViewModel(
            itemRepository = itemRepository
        )
        advanceUntilIdle()

        // Then
        val expected = ItemsUiState(
            isLoading = false,
            itemsError = "Network error: Failed to fetch items (Code: 404)"
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `onSetSearchValue() should update uiState with filtered items`() = runTest {

        val expected = "Item B"
        viewModel.onSetSearchValue(expected)

        val actual = viewModel.uiState.value.searchFieldValue
        assertEquals(expected, actual)
    }

    @Test
    fun `onSelectTier() should update uiState with filtered items`() = runTest {

        val expected = "Tier 1"
        viewModel.onSelectTier(expected)

        val actual = viewModel.uiState.value.selectedTierFilter
        assertEquals(expected, actual)
    }

    @Test
    fun `onClearTier() should set selectedTierFilter to null`() = runTest {
        val tier = "Tier 1"

        viewModel.onSelectTier(tier)
        assertEquals(
            tier,
            viewModel.uiState.value.selectedTierFilter
        )

        viewModel.onClearTier()

        assertNull(viewModel.uiState.value.selectedTierFilter)
    }

    @Test
    fun `onSelectStat() should update uiState with stat filter`() = runTest {

        val expected = "Stat 1"
        viewModel.onSelectStat(expected)

        val actual = viewModel.uiState.value.selectedStatFilters
        assertEquals(listOf(expected), actual)
    }

    @Test
    fun `onSelectStat() should remove stat from list if it exists`() = runTest {

        val expected = "Stat 1"
        viewModel.onSelectStat(expected)

        assertEquals(
            listOf(expected),
            viewModel.uiState.value.selectedStatFilters
        )

        viewModel.onSelectStat(expected)
        assertEquals(
            emptyList<String>(),
            viewModel.uiState.value.selectedStatFilters
        )
    }

    @Test
    fun `onClearStats() should remove all stats from list`() = runTest {
        val expected = "Stat 1"
        viewModel.onSelectStat(expected)

        assertEquals(
            listOf(expected),
            viewModel.uiState.value.selectedStatFilters
        )

        viewModel.onSelectStat(expected)
        assertEquals(
            emptyList<String>(),
            viewModel.uiState.value.selectedStatFilters
        )
    }

    @Test
    fun `onClearSearch() should set searchFieldValue to empty string`() = runTest {
        val expected = "Item B"
        viewModel.onSetSearchValue(expected)

        assertEquals(
            expected,
            viewModel.uiState.value.searchFieldValue
        )

        viewModel.onClearSearch()
        assertEquals(
            "",
            viewModel.uiState.value.searchFieldValue
        )
    }

    @Test
    fun `getFilteredItems() should return filtered items by tier`() = runTest {


        assertEquals(
            listOf(
                fakeItemDto2.asItemDetails(),
                fakeItemDto3.asItemDetails(),
                fakeItemDto4.asItemDetails(),
            ),
            viewModel.uiState.value.filteredItems
        )

        viewModel.onSelectTier("Tier III")
        viewModel.getFilteredItems()

        assertEquals(
            listOf(
                fakeItemDto2.asItemDetails(),
                fakeItemDto3.asItemDetails(),
                fakeItemDto4.asItemDetails()
            ),
            viewModel.uiState.value.filteredItems
        )
    }

    @Test
    fun `getFilteredItems() should return filtered items by tier and stats`() = runTest {

        `getFilteredItems() should return filtered items by tier`()

        viewModel.onSelectStat("Lifesteal")
        viewModel.getFilteredItems()

        assertEquals(
            listOf(
                fakeItemDto2.asItemDetails(),
                fakeItemDto3.asItemDetails(),
            ),
            viewModel.uiState.value.filteredItems
        )
    }

    @Test
    fun `getFilteredItems() should return filtered items by tier, stats, and search`() {
        `getFilteredItems() should return filtered items by tier and stats`()

        viewModel.onSetSearchValue("Item C")
        viewModel.getFilteredItems()

        assertEquals(
            listOf(
                fakeItemDto3.asItemDetails(),
            ),
            viewModel.uiState.value.filteredItems
        )
    }


}