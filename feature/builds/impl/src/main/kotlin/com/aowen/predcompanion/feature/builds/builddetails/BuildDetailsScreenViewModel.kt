package com.aowen.predcompanion.feature.builds.builddetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.model.mapper.BuildListItemUiMapper
import com.aowen.predcompanion.core.data.model.mapper.BuildUiListItem
import com.aowen.predcompanion.core.data.repository.builds.BuildRepository
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.data.repository.user.UserFavoriteBuildsRepository
import com.aowen.predcompanion.core.datastore.UserPreferencesManager
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.data.Console
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BuildDetailsErrors(
    val errorMessage: String? = null,
)

data class BuildDetailsUiState(
    val isLoading: Boolean = true,
    val buildDetails: BuildUiListItem? = null,
    val isFavorited: Boolean = false,
    val items: List<ItemDetails> = emptyList(),
    val selectedItemDetails: ItemDetails? = null,
    val error: BuildDetailsErrors? = null
)

@HiltViewModel(assistedFactory = BuildDetailsScreenViewModel.Factory::class)
class BuildDetailsScreenViewModel @AssistedInject constructor(
    @Assisted private val buildId: String,
    private val userPreferencesDataStore: UserPreferencesManager,
    private val userFavoriteBuildsRepository: UserFavoriteBuildsRepository,
    private val omedaCityItemRepository: ItemRepository,
    private val omedaCityBuildRepository: BuildRepository,
    private val buildDetailsUiMapper: BuildListItemUiMapper
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(buildId: String): BuildDetailsScreenViewModel
    }


    private val _uiState = MutableStateFlow(BuildDetailsUiState())
    val uiState = _uiState

    private val _console = MutableStateFlow(Console.PC)
    val console = _console

    val allItems = omedaCityItemRepository.allItems.value

    init {
        initViewModel()
    }

    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            _console.emit(userPreferencesDataStore.console.first())
            val buildsDeferred = async { omedaCityBuildRepository.fetchBuildById(buildId) }

            val favoritedBuilds = userFavoriteBuildsRepository.fetchFavoriteBuildIds()

            if (favoritedBuilds.isSuccess) {
                val isFavorited =
                    favoritedBuilds.getOrNull()?.any { it == buildId.toInt() } == true
                _uiState.update {
                    it.copy(isFavorited = isFavorited)
                }
            }

            try {
                val buildDetails = buildDetailsUiMapper.buildFrom(buildsDeferred.await().getOrThrow())

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        buildDetails = buildDetails,
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

    fun onAddBuildToFavorites(build: BuildUiListItem) {
        viewModelScope.launch {
            userFavoriteBuildsRepository.addFavoriteBuild(build)
            _uiState.update {
                it.copy(isFavorited = true)
            }
        }
    }

    fun onRemoveBuildFromFavorites(build: BuildUiListItem) {
        viewModelScope.launch {
            userFavoriteBuildsRepository.removeFavoriteBuild(build.buildId)
            _uiState.update {
                it.copy(isFavorited = false)
            }
        }
    }

    fun onItemClicked(itemId: Int) {
        _uiState.update {
            it.copy(selectedItemDetails = omedaCityItemRepository.getItemById(itemId))
        }
    }
}