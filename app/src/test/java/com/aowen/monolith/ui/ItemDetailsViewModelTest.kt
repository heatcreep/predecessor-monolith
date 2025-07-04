@file:OptIn(ExperimentalCoroutinesApi::class)
package com.aowen.monolith.ui

import androidx.lifecycle.SavedStateHandle
import com.aowen.monolith.data.asItemDetails
import com.aowen.monolith.fakes.data.fakeItemDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityItemRepository
import com.aowen.monolith.feature.items.itemdetails.ItemDetailsUiState
import com.aowen.monolith.feature.items.itemdetails.ItemDetailsViewModel
import com.aowen.monolith.network.Resource
import com.aowen.monolith.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ItemDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val dispatcher = mainDispatcherRule.testDispatcher

    private lateinit var viewModel: ItemDetailsViewModel

    private var itemRepository = FakeOmedaCityItemRepository()

    @Test
    fun `initViewModel() should set uiState to correct state`() = runTest(dispatcher) {
        viewModel = ItemDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "itemName" to "Vanquisher"
                )

            ),
            itemRepository = itemRepository
        )

        advanceUntilIdle()

        val expected = ItemDetailsUiState.Loaded(item = fakeItemDto.asItemDetails())
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }


    @Test
    fun `initViewModel() should set uiState to correct state with error`() = runTest {
        itemRepository = mockk()
        coEvery { itemRepository.fetchItemByName(any()) } returns Resource.NetworkError(404, "Failed to fetch item")
        viewModel = ItemDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "itemName" to "Error"
                )

            ),
            itemRepository = itemRepository
        )

        advanceUntilIdle()

        val expected = ItemDetailsUiState.Error(message = "Network error: Failed to fetch item (Code: 404)")
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }
}