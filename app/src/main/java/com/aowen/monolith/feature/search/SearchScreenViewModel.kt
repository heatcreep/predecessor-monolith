package com.aowen.monolith.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.MatchDetails
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.repository.builds.BuildRepository
import com.aowen.monolith.data.repository.heroes.HeroRepository
import com.aowen.monolith.data.repository.items.ItemRepository
import com.aowen.monolith.data.repository.matches.MatchRepository
import com.aowen.monolith.logDebug
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.Resource
import com.aowen.monolith.network.UserRecentSearchRepository
import com.aowen.monolith.network.getOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AllItemsState {
    data class Success(val items: List<ItemDetails>) : AllItemsState
    data class Error(val errorMessage: String?) : AllItemsState
    data object Empty : AllItemsState
    data object Loading : AllItemsState
}

sealed interface AllHeroesState {
    data class Success(val heroes: List<HeroDetails>) : AllHeroesState
    data class Error(val errorMessage: String?) : AllHeroesState
    data object Empty : AllHeroesState
    data object Loading : AllHeroesState
}

data class SearchScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val searchError: String? = null,
    val recentSearchError: String? = null,
    val isLoadingSearch: Boolean = true,
    val isLoadingRecentSearches: Boolean = true,
    val searchFieldValue: String = "",
    val isLoadingItemsAndHeroes: Boolean = true,
    val allItems: AllItemsState = AllItemsState.Loading,
    val filteredItems: List<ItemDetails> = emptyList(),
    val itemsError: String? = null,
    val allHeroes: AllHeroesState = AllHeroesState.Loading,
    val filteredHeroes: List<HeroDetails> = emptyList(),
    val heroesError: String? = null,
    val filteredBuilds: List<BuildListItem> = emptyList(),
    val buildsError: String? = null,
    val isLoadingMatchSearch: Boolean = true,
    val foundMatch: MatchDetails? = null,
    val matchesError: String? = null,
    val playersList: List<PlayerDetails?> = emptyList(),
    val recentSearchesList: List<PlayerDetails?> = emptyList(),
)

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val omedaCityRepository: OmedaCityRepository,
    private val omedaCityHeroRepository: HeroRepository,
    private val omedaCityItemRepository: ItemRepository,
    private val omedaCityMatchRepository: MatchRepository,
    private val omedaCityBuildRepository: BuildRepository,
    private val userRecentSearchesRepository: UserRecentSearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState = _uiState

    init {
        initViewModel()
    }

    fun initViewModel() {
        viewModelScope.launch {
            val recentSearchesDeferredResult =
                async { userRecentSearchesRepository.getRecentSearches() }

            val itemsDeferredResult =
                async { omedaCityItemRepository.fetchAllItems() }

            val heroesDeferredResult =
                async { omedaCityHeroRepository.fetchAllHeroes() }

            val recentSearches = recentSearchesDeferredResult.await()
            val itemsResult = itemsDeferredResult.await()
            val heroesResource = heroesDeferredResult.await()

            val heroesState = when (heroesResource) {
                is Resource.Success -> {
                    if (heroesResource.data.isEmpty()) {
                        AllHeroesState.Empty
                    } else {
                        AllHeroesState.Success(heroesResource.data)
                    }
                }

                is Resource.NetworkError -> {
                    AllHeroesState.Error(heroesResource.errorMessage)
                }

                is Resource.GenericError -> {
                    AllHeroesState.Error(heroesResource.errorMessage)
                }
            }

            val itemsState = when (itemsResult) {
                is Resource.Success -> {
                    if (itemsResult.data.isEmpty()) {
                        AllItemsState.Empty
                    } else {
                        AllItemsState.Success(itemsResult.data)
                    }
                }

                is Resource.NetworkError -> {
                    AllItemsState.Error(itemsResult.errorMessage)
                }

                is Resource.GenericError -> {
                    AllItemsState.Error(itemsResult.errorMessage)
                }
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isLoadingRecentSearches = false,
                    isLoadingItemsAndHeroes = false,
                    recentSearchesList = recentSearches,
                    allItems = itemsState,
                    allHeroes = heroesState
                )
            }
        }
    }

    fun setSearchValue(text: String) {
        _uiState.update {
            it.copy(
                searchFieldValue = text
            )
        }
    }

    fun handleClearSearch() {
        _uiState.update {
            it.copy(
                searchFieldValue = "",
                playersList = emptyList(),
                searchError = null
            )
        }
    }

    fun handleClearSingleRecentSearch(playerId: String) {
        viewModelScope.launch {
            try {
                userRecentSearchesRepository.removeRecentSearch(playerId)
                _uiState.update {
                    it.copy(
                        recentSearchesList = it.recentSearchesList.filter { player ->
                            player?.playerId != playerId
                        }
                    )
                }
            } catch (e: Exception) {
                logDebug(e.toString())
            }
        }
    }

    fun handleClearAllRecentSearches() {
        _uiState.update { it.copy(isLoadingSearch = true) }
        viewModelScope.launch {
            try {
                val removeAllResult =
                    async { userRecentSearchesRepository.removeAllRecentSearches() }
                removeAllResult.await()
                _uiState.update {
                    it.copy(
                        recentSearchesList = emptyList()
                    )
                }
            } catch (e: Exception) {
                logDebug(e.toString())
            }
            _uiState.update { it.copy(isLoadingSearch = false) }
        }
    }

    fun handleAddToRecentSearch(playerDetails: PlayerDetails) {
        viewModelScope.launch {
            try {
                val addRecentSearchDeferred =
                    async { userRecentSearchesRepository.addRecentSearch(playerDetails) }
                addRecentSearchDeferred.await()
                _uiState.update {
                    it.copy(
                        recentSearchesList = it.recentSearchesList + playerDetails
                    )
                }
            } catch (e: Exception) {
                logDebug(e.toString())
            }
        }
    }

    fun handleSubmitSearch() {
        _uiState.update { it.copy(isLoadingSearch = true, isLoadingMatchSearch = true) }
        val itemsState = (uiState.value.allItems as AllItemsState.Success)
        val heroesState = (uiState.value.allHeroes as AllHeroesState.Success)
        val fieldValue = uiState.value.searchFieldValue.trim()
        val itemsList = itemsState.items.filter { item ->
            item.displayName.contains(fieldValue, ignoreCase = true)
        }
        val heroesList = heroesState.heroes.filter { hero ->
            hero.displayName.contains(fieldValue, ignoreCase = true)
        }
        _uiState.update {
            it.copy(
                filteredHeroes = heroesList,
                filteredItems = itemsList,
                isLoadingItemsAndHeroes = false
            )
        }
        viewModelScope.launch {
            val playersListDeferredResult =
                async { omedaCityRepository.fetchPlayersByName(fieldValue) }
            val buildsListDeferredResult = async {
                omedaCityBuildRepository.fetchAllBuilds(
                    name = fieldValue
                )
            }
            val matchesListDeferredResult =
                async { omedaCityMatchRepository.fetchMatchById(fieldValue) }

            val playersListResult = playersListDeferredResult.await()

            if (playersListResult.isFailure) {
                _uiState.update {
                    it.copy(
                        searchError = playersListResult.exceptionOrNull()?.message
                    )
                }
            }
            val filteredPlayersList = playersListResult.getOrNull()?.filter {
                !it.isCheater && !it.isMmrDisabled
            }
            try {
                val buildsList = buildsListDeferredResult.await().getOrThrow()
                val matchesList = matchesListDeferredResult.await().getOrThrow()
                _uiState.update {
                    it.copy(
                        isLoadingSearch = false,
                        isLoadingMatchSearch = false,
                        matchesError = null,
                        playersList = filteredPlayersList ?: emptyList(),
                        foundMatch = matchesList,
                        filteredBuilds = buildsList
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingSearch = false,
                        isLoadingMatchSearch = false,
                        matchesError = e.message,
                        playersList = filteredPlayersList ?: emptyList(),
                    )
                }
            }
        }
    }
}