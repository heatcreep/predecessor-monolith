package com.aowen.monolith.feature.builds.builddetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.Console
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.UserFavoriteBuildsRepository
import com.aowen.monolith.network.UserPreferencesManager
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
    private val repository: OmedaCityRepository
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
            val buildsDeferred = async { repository.fetchBuildById(buildId) }
            val itemsDeferred = async { repository.fetchAllItems() }

            val favoritedBuilds = userFavoriteBuildsRepository.fetchFavoriteBuilds()

            if (favoritedBuilds.isSuccess) {
                val isFavorited =
                    favoritedBuilds.getOrNull()?.any { it.buildId == buildId.toInt() } ?: false
                _uiState.update {
                    it.copy(isFavorited = isFavorited)
                }
            }

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