package com.aowen.predcompanion.feature.items.impl.itemdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface ItemDetailsUiState {
    data object Loading : ItemDetailsUiState
    data class Loaded(val item: ItemDetails) : ItemDetailsUiState
    data class Error(val message: String?) : ItemDetailsUiState
}

@HiltViewModel(assistedFactory = ItemDetailsViewModel.Factory::class)
class ItemDetailsViewModel @AssistedInject constructor(
    @Assisted("itemName") private val itemName: String,
    val itemRepository: ItemRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("itemName") itemName: String): ItemDetailsViewModel
    }

    private val _uiState = MutableStateFlow<ItemDetailsUiState>(ItemDetailsUiState.Loading)
    val uiState: StateFlow<ItemDetailsUiState> = _uiState


    init {
        initViewModel()
    }

    fun initViewModel() {
        viewModelScope.launch {
            // Ensure the cache is warm (no-op if already populated from app startup).
            itemRepository.fetchAllItems()
            val item = itemRepository.getItemByName(itemName)
            _uiState.value = if (item != null) {
                ItemDetailsUiState.Loaded(item = item)
            } else {
                ItemDetailsUiState.Error(message = "Item '$itemName' not found")
            }
        }
    }
}