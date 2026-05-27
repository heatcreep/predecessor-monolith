@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.predcompanion.feature.items.impl

import com.aowen.predcompanion.core.model.data.asItemDetails
import com.aowen.predcompanion.core.testing.fakes.data.network.fakeNetworkItem
import com.aowen.predcompanion.core.testing.fakes.repository.FakeOmedaCityItemRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import com.aowen.predcompanion.feature.items.impl.itemdetails.ItemDetailsUiState
import com.aowen.predcompanion.feature.items.impl.itemdetails.ItemDetailsViewModel
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ItemDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var viewModel: ItemDetailsViewModel

    private var itemRepository = FakeOmedaCityItemRepository()

    @Test
    fun `initViewModel() should set uiState to correct state`() = runTest {
        viewModel = ItemDetailsViewModel(
            itemName = fakeNetworkItem.name,
            itemRepository = itemRepository
        )

        advanceUntilIdle()

        val expected = ItemDetailsUiState.Loaded(item = fakeNetworkItem.asItemDetails())
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }


    @Test
    fun `initViewModel() should set uiState to correct state with error`() = testScope.runTest {
        itemRepository = mockk()
        coEvery { itemRepository.fetchAllItems() } just runs
        coEvery { itemRepository.getItemByName(any()) } returns null
        viewModel = ItemDetailsViewModel(
            itemName = "Error",
            itemRepository = itemRepository
        )

        advanceUntilIdle()

        val expected = ItemDetailsUiState.Error(message = "Item 'Error' not found")
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }
}