package com.aowen.monolith.feature.items.itemdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.repository.items.ItemRepository
import com.aowen.monolith.network.getOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ItemDetailsUiState {
    data object Loading : ItemDetailsUiState
    data class Loaded(val item: ItemDetails) : ItemDetailsUiState
    data class Error(val message: String?) : ItemDetailsUiState
}

@HiltViewModel
class ItemDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ItemDetailsUiState>(ItemDetailsUiState.Loading)
    val uiState: StateFlow<ItemDetailsUiState> = _uiState

    private val itemName: String = checkNotNull(savedStateHandle["itemName"])

    init {
        initViewModel()
    }

    fun initViewModel() {
        viewModelScope.launch {
            val itemResponse = itemRepository.fetchItemByName(itemName)
            try {
                _uiState.value = ItemDetailsUiState.Loaded(
                    item = itemResponse.getOrThrow()
                )
            } catch (e: Exception) {
                _uiState.value = ItemDetailsUiState.Error(
                    message = e.message
                )
            }
        }
    }
}