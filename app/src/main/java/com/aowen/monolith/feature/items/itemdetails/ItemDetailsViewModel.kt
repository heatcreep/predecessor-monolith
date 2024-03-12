package com.aowen.monolith.feature.items.itemdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.network.OmedaCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ItemDetailsUiState(
    val isLoading: Boolean = true,
    val item: ItemDetails = ItemDetails(),
    val error: String? = null
)

@HiltViewModel
class ItemDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val repository: OmedaCityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemDetailsUiState())
    val uiState: StateFlow<ItemDetailsUiState> = _uiState

    private val itemName: String = checkNotNull(savedStateHandle["itemName"])

    init {
        viewModelScope.launch {
            initViewModel()
        }
    }

    suspend fun initViewModel() {
        _uiState.value = ItemDetailsUiState(isLoading = true)
        val itemResponse = repository.fetchItemByName(itemName)
        if (itemResponse.isSuccess) {
            val item = itemResponse.getOrNull()
            if (item == null) {
                _uiState.value = ItemDetailsUiState(
                    isLoading = false,
                    error = "Item was null"
                )
            } else {
                _uiState.value = ItemDetailsUiState(
                    isLoading = false,
                    item = item
                )
            }
        } else {
            _uiState.value = ItemDetailsUiState(
                isLoading = false,
                error = itemResponse.exceptionOrNull()?.message
            )
        }

    }
}