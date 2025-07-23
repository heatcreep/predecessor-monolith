package com.aowen.monolith.feature.builds.builddetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.Console
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.repository.builds.BuildRepository
import com.aowen.monolith.data.repository.items.ItemRepository
import com.aowen.monolith.network.UserFavoriteBuildsRepository
import com.aowen.monolith.network.UserPreferencesManager
import com.aowen.monolith.network.getOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BuildDetailsErrors(
    val errorMessage: String? = null,
)

data class BuildDetailsUiState(
    val isLoading: Boolean = true,
    val buildDetails: BuildListItem? = null,
    val isFavorited: Boolean = false,
    val items: List<ItemDetails> = emptyList(),
    val selectedItemDetails: ItemDetails? = null,
    val error: BuildDetailsErrors? = null
)

@HiltViewModel
class BuildDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userPreferencesDataStore: UserPreferencesManager,
    private val userFavoriteBuildsRepository: UserFavoriteBuildsRepository,
    private val omedaCityItemRepository: ItemRepository,
    private val omedaCityBuildRepository: BuildRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BuildDetailsUiState())
    val uiState = _uiState

    private val _console = MutableStateFlow(Console.PC)
    val console = _console

    private val buildId: String = checkNotNull(savedStateHandle["buildId"])

    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            _console.emit(userPreferencesDataStore.console.first())
            val buildsDeferred = async { omedaCityBuildRepository.fetchBuildById(buildId) }
            val itemsDeferred = async { omedaCityItemRepository.fetchAllItems() }

            val favoritedBuilds = userFavoriteBuildsRepository.fetchFavoriteBuildIds()

            if (favoritedBuilds.isSuccess) {
                val isFavorited =
                    favoritedBuilds.getOrNull()?.any { it == buildId.toInt() } == true
                _uiState.update {
                    it.copy(isFavorited = isFavorited)
                }
            }

            try {
                val buildDetails = buildsDeferred.await().getOrThrow()
                val allItems = itemsDeferred.await().getOrThrow()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        buildDetails = buildDetails,
                        items = allItems,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = BuildDetailsErrors(
                            errorMessage = e.message
                        )
                    )
                }
            }
        }
    }

    fun onAddBuildToFavorites(build: BuildListItem) {
        viewModelScope.launch {
            userFavoriteBuildsRepository.addFavoriteBuild(build)
            _uiState.update {
                it.copy(isFavorited = true)
            }
        }
    }

    fun onRemoveBuildFromFavorites(build: BuildListItem) {
        viewModelScope.launch {
            userFavoriteBuildsRepository.removeFavoriteBuild(build.id)
            _uiState.update {
                it.copy(isFavorited = false)
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