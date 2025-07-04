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
import com.aowen.monolith.data.repository.players.di.PlayerRepository
import com.aowen.monolith.logDebug
import com.aowen.monolith.network.Resource
import com.aowen.monolith.network.UserRecentSearchRepository
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

sealed interface PlayersListState {
    data class Success(val players: List<PlayerDetails>) : PlayersListState
    data class Error(val errorMessage: String?) : PlayersListState
    data object Empty : PlayersListState
    data object Loading : PlayersListState
}

sealed interface BuildsListState {
    data class Success(val builds: List<BuildListItem>) : BuildsListState
    data class Error(val errorMessage: String?) : BuildsListState
    data object Empty : BuildsListState
    data object Loading : BuildsListState
}

sealed interface MatchSearchState {
    data class Success(val match: MatchDetails) : MatchSearchState
    data class Error(val errorMessage: String?) : MatchSearchState
    data object None : MatchSearchState
    data object Loading : MatchSearchState
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
    val filteredBuilds: BuildsListState = BuildsListState.Empty,
    val buildsError: String? = null,
    val isLoadingMatchSearch: Boolean = true,
    val foundMatch: MatchSearchState = MatchSearchState.None,
    val matchesError: String? = null,
    val playersList: PlayersListState = PlayersListState.Empty,
    val recentSearchesList: List<PlayerDetails?> = emptyList(),
)

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val omedaCityBuildRepository: BuildRepository,
    private val omedaCityHeroRepository: HeroRepository,
    private val omedaCityItemRepository: ItemRepository,
    private val omedaCityMatchRepository: MatchRepository,
    private val omedaCityPlayerRepository: PlayerRepository,
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
                playersList = PlayersListState.Empty,
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
                if (!uiState.value.recentSearchesList.any { it?.playerId == playerDetails.playerId }) {
                    _uiState.update {
                        it.copy(
                            recentSearchesList = it.recentSearchesList.plus(playerDetails)
                        )
                    }
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
            _uiState.update {
                it.copy(
                    playersList = PlayersListState.Loading,
                    filteredBuilds = BuildsListState.Loading,
                    foundMatch = MatchSearchState.Loading
                )
            }
            val playersListDeferredResult =
                async { omedaCityPlayerRepository.fetchPlayersByName(fieldValue) }
            val buildsListDeferredResult = async {
                omedaCityBuildRepository.fetchAllBuilds(
                    name = fieldValue
                )
            }
            val matchesListDeferredResult =
                async { omedaCityMatchRepository.fetchMatchById(fieldValue) }

            val playersResource = playersListDeferredResult.await()
            val buildsResource = buildsListDeferredResult.await()
            val matchesResource = matchesListDeferredResult.await()


            // Handle Players State
            val playersState = when (playersResource) {
                is Resource.Success -> {
                    if (playersResource.data.isEmpty()) {
                        PlayersListState.Empty
                    } else {
                        val filteredPlayersList = playersResource.data.filter {
                            !it.isCheater && !it.isMmrDisabled
                        }
                        PlayersListState.Success(filteredPlayersList)
                    }
                }

                is Resource.NetworkError -> {
                    PlayersListState.Error(playersResource.errorMessage)
                }

                is Resource.GenericError -> {
                    PlayersListState.Error(playersResource.errorMessage)
                }
            }

            // Handle Builds State
            val buildsListState = when (buildsResource) {
                is Resource.Success -> {
                    if (buildsResource.data.isEmpty()) {
                        BuildsListState.Empty
                    } else {
                        BuildsListState.Success(buildsResource.data)
                    }
                }

                is Resource.NetworkError -> {
                    BuildsListState.Error(buildsResource.errorMessage)
                }

                is Resource.GenericError -> {
                    BuildsListState.Error(buildsResource.errorMessage)
                }
            }

            // Handle Matches State
            val foundMatchState = when (matchesResource) {
                is Resource.Success -> {
                    if (matchesResource.data == null) {
                        MatchSearchState.None
                    } else {
                        MatchSearchState.Success(matchesResource.data)
                    }
                }

                is Resource.NetworkError -> {
                    MatchSearchState.Error(matchesResource.errorMessage)
                }

                is Resource.GenericError -> {
                    MatchSearchState.Error(matchesResource.errorMessage)
                }
            }
            _uiState.update {
                it.copy(
                    isLoadingSearch = false,
                    isLoadingMatchSearch = false,
                    matchesError = null,
                    playersList = playersState,
                    foundMatch = foundMatchState,
                    filteredBuilds = buildsListState
                )
            }
        }
    }
}