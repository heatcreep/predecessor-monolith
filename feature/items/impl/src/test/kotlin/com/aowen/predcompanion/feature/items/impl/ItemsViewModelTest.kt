@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.predcompanion.feature.items.impl

import com.aowen.predcompanion.core.model.data.asItemDetails
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem2
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem3
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem4
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityItemRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
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
                fakeNetworkItem2.asItemDetails(),
                fakeNetworkItem.asItemDetails(),
                fakeNetworkItem3.asItemDetails(),
                fakeNetworkItem4.asItemDetails()
            ),
            filteredItems = listOf(
                fakeNetworkItem2.asItemDetails(),
                fakeNetworkItem3.asItemDetails(),
                fakeNetworkItem4.asItemDetails(),
            ),
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set uiState to ItemsUiState with error`() = runTest {
        itemRepository = mockk()
        coEvery { itemRepository.getAllItems() } returns emptyList()
        viewModel = ItemsViewModel(
            itemRepository = itemRepository
        )
        advanceUntilIdle()

        // Then
        val expected = ItemsUiState(
            isLoading = false,
            itemsError = "Failed to fetch items"
        )
        val actual = viewModel.uiState.value
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
    fun `getFilteredItems() should return filtered items by tier`() = runTest {


        assertEquals(
            listOf(
                fakeNetworkItem2.asItemDetails(),
                fakeNetworkItem3.asItemDetails(),
                fakeNetworkItem4.asItemDetails(),
            ),
            viewModel.uiState.value.filteredItems
        )

        viewModel.onSelectTier("Tier III")
        viewModel.getFilteredItems()

        assertEquals(
            listOf(
                fakeNetworkItem2.asItemDetails(),
                fakeNetworkItem3.asItemDetails(),
                fakeNetworkItem4.asItemDetails()
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
                fakeNetworkItem2.asItemDetails(),
                fakeNetworkItem3.asItemDetails(),
            ),
            viewModel.uiState.value.filteredItems
        )
    }


}