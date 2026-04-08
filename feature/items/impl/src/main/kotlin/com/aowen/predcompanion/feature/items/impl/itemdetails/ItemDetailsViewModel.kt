package com.aowen.predcompanion.feature.items.impl.itemdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.data.ItemDetails
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