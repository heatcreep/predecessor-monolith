package com.aowen.predcompanion.feature.builds.builddetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.repository.builds.BuildRepository
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.core.ui.model.mapper.BuildListItemUiMapper
import com.aowen.predcompanion.core.ui.model.mapper.BuildUiListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BuildDetailsErrors(
    val errorMessage: String? = null,
)

sealed interface BuildDetailsState {
    data object Loading : BuildDetailsState
    data class Error(val errorMessage: String) : BuildDetailsState
    data class Success(
        val buildDetails: BuildUiListItem,
    ) : BuildDetailsState
}

data class BuildDetailsUiState(
    val buildDetailsState: BuildDetailsState = BuildDetailsState.Loading,
    val selectedItemDetails: ItemDetails? = null,
)

@HiltViewModel(assistedFactory = BuildDetailsScreenViewModel.Factory::class)
class BuildDetailsScreenViewModel @AssistedInject constructor(
    @Assisted private val buildId: String,
    private val omedaCityItemRepository: ItemRepository,
    private val omedaCityBuildRepository: BuildRepository,
    private val buildDetailsUiMapper: BuildListItemUiMapper
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(buildId: String): BuildDetailsScreenViewModel
    }

    private val _uiState: MutableStateFlow<BuildDetailsUiState> = MutableStateFlow(
        BuildDetailsUiState()
    )
    val uiState: StateFlow<BuildDetailsUiState> = _uiState

    init {
        initViewModel()
    }

    fun initViewModel() {
        viewModelScope.launch {
            val buildsDeferred = async { omedaCityBuildRepository.fetchBuildById(buildId) }


            try {
                val buildDetails =
                    buildDetailsUiMapper.buildFrom(buildsDeferred.await().getOrThrow())

                _uiState.update {
                    it.copy(
                        buildDetailsState = BuildDetailsState.Success(buildDetails)
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        buildDetailsState = BuildDetailsState.Error(e.message ?: "Error")
                    )
                }
            }
        }
    }

    fun onItemClicked(itemId: Int) {
        _uiState.update {
            it.copy(selectedItemDetails = omedaCityItemRepository.getItemById(itemId.toString()))
        }
    }
}