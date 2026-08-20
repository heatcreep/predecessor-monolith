package com.aowen.predcompanion.feature.collections.impl.itemdetails

import app.cash.turbine.test
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.testing.fakes.repository.FakeItemRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ItemDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fakeItemRepository = FakeItemRepository()

    private fun createViewModel(itemName: String) = ItemDetailsViewModel(
        itemName = itemName,
        itemRepository = fakeItemRepository,
    )

    private val testItem = ItemDetails(
        id = "1",
        name = "Ashenblade",
        displayName = "Ashenblade",
    )

    @Test
    fun `when item exists, state is loaded with item`() = runTest {
        fakeItemRepository.itemsToReturn = listOf(testItem)

        val viewModel = createViewModel("Ashenblade")

        viewModel.uiState.test {
            val loaded = awaitItem() as ItemDetailsUiState.Loaded
            assertEquals("Ashenblade", loaded.item.displayName)
        }
    }

    @Test
    fun `when item does not exist, state is error`() = runTest {
        fakeItemRepository.itemsToReturn = emptyList()

        val viewModel = createViewModel("NonExistent")

        viewModel.uiState.test {
            val error = awaitItem() as ItemDetailsUiState.Error
            assertTrue(error.message!!.contains("NonExistent"))
        }
    }

    @Test
    fun `initViewModel reloads item`() = runTest {
        fakeItemRepository.itemsToReturn = emptyList()

        val viewModel = createViewModel("Ashenblade")

        viewModel.uiState.test {
            assertTrue(awaitItem() is ItemDetailsUiState.Error)

            fakeItemRepository.itemsToReturn = listOf(testItem)
            viewModel.initViewModel()

            val loaded = awaitItem() as ItemDetailsUiState.Loaded
            assertEquals("Ashenblade", loaded.item.displayName)
        }
    }
}
