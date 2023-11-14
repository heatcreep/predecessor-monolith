package com.aowen.monolith.ui

import androidx.lifecycle.SavedStateHandle
import com.aowen.monolith.data.create
import com.aowen.monolith.fakes.data.fakeItemDto
import com.aowen.monolith.fakes.repo.FakeOmedaCityRepository
import com.aowen.monolith.fakes.repo.ResponseType
import com.aowen.monolith.ui.screens.items.ItemDetailsUiState
import com.aowen.monolith.ui.screens.items.ItemDetailsViewModel
import com.aowen.monolith.utils.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ItemDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ItemDetailsViewModel

    @Test
    fun `initViewModel() should set uiState to correct state`() = runTest {
        viewModel = ItemDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "itemName" to "Vanquisher"
                )

            ),
            repository = FakeOmedaCityRepository()
        )

        viewModel.initViewModel()

        val expected = ItemDetailsUiState(
            isLoading = false,
            item = fakeItemDto.create()
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set uiState to correct state with empty item`() = runTest {
        viewModel = ItemDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "itemName" to "Empty"
                )

            ),
            repository = FakeOmedaCityRepository(
                itemDetailsResponse = ResponseType.Empty
            )
        )

        viewModel.initViewModel()

        val expected = ItemDetailsUiState(
            isLoading = false,
            error = "Item was null"
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }

    @Test
    fun `initViewModel() should set uiState to correct state with error`() = runTest {
        viewModel = ItemDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "itemName" to "Error"
                )

            ),
            repository = FakeOmedaCityRepository(
                hasItemDetailsErrors = true
            )
        )

        viewModel.initViewModel()

        val expected = ItemDetailsUiState(
            isLoading = false,
            error = "Failed to fetch item"
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual)
    }
}