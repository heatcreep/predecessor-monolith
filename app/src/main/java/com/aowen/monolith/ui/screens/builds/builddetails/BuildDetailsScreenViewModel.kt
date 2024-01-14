package com.aowen.monolith.ui.screens.builds.builddetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.network.OmedaCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BuildDetailsErrors(
    val errorMessage: String? = null,
)

data class BuildDetailsUiState(
    val isLoading: Boolean = true,
    val buildDetails: BuildListItem? = null,
    val items: List<ItemDetails> = emptyList(),
    val selectedItemDetails: ItemDetails? = null,
    val error: BuildDetailsErrors? = null
)

@HiltViewModel
class BuildDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: OmedaCityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BuildDetailsUiState())
    val uiState = _uiState

    private val buildId: String = checkNotNull(savedStateHandle["buildId"])

    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val buildsDeferred = async { repository.fetchBuildById(buildId) }
            val itemsDeferred = async { repository.fetchAllItems() }

            val buildsResult = buildsDeferred.await()
            val itemsResult = itemsDeferred.await()

            if (buildsResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = BuildDetailsErrors(
                            errorMessage = buildsResult.exceptionOrNull()?.message
                        )
                    )
                }
                return@launch
            }

            if (itemsResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = BuildDetailsErrors(
                            errorMessage = itemsResult.exceptionOrNull()?.message
                        )
                    )
                }
                return@launch
            }

            val buildDetails = buildsResult.getOrNull()
            val allItems = itemsResult.getOrNull()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    buildDetails = buildDetails,
                    items = allItems ?: emptyList(),
                )
            }

        }
    }

    fun onItemClicked(itemId: Int) {
        _uiState.update {
            it.copy(selectedItemDetails = uiState.value.items.firstOrNull { itemDetails ->
                itemDetails.id == itemId
            })
        }
    }
}