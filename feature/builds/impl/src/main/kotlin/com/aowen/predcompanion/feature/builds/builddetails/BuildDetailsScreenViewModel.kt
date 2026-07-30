package com.aowen.predcompanion.feature.builds.builddetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.repository.builds.BuildRepository
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.data.repository.user.UserFavoriteBuildsRepository
import com.aowen.predcompanion.core.datastore.UserPreferencesManager
import com.aowen.predcompanion.core.model.data.HeroBuild
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

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

    val foo = MutableStateFlow("")

    val bar: StateFlow<String> = foo
        .debounce(500L)
        .flatMapLatest {
            // Do something with the value
            if (it.isEmpty()) return@flatMapLatest flow { emit("") }
            flow { emit(it) }
        }.catch { e ->
            emit("Error: ${e.message}")
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")


    private val _uiState = MutableStateFlow(BuildDetailsUiState())
    val uiState = _uiState

    init {
        initViewModel()
    }

    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
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
            userFavoriteBuildsRepository.addFavoriteBuild(build.toHeroBuild())
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
            it.copy(selectedItemDetails = omedaCityItemRepository.getItemById(itemId.toString()))
        }
    }

    private fun BuildUiListItem.toHeroBuild(): HeroBuild =
        HeroBuild(
            id = buildId,
            title = title,
            author = author,
            role = role?.name ?: "unknown",
            description = description,
            heroId = heroId,
            crestId = crest.id.toInt(),
            buildItemIds = buildItems.map { it.id.toInt() },
            skillOrder = skillOrder,
            netVotes = 0,
            upvotes = 0,
            downvotes = 0,
            modules = modules.map {
                HeroBuild.ItemModule(
                    id = UUID.randomUUID().toString(),
                    title = it.title,
                    itemIds = it.items.map { item -> item.id.toInt() }
                )
            },
            createdAt = createdAt,
            updatedAt = updatedAt,
            version = version
        )
}