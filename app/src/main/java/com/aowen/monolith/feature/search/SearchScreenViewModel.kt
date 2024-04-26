package com.aowen.monolith.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.logDebug
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.UserRecentSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val searchError: String? = null,
    val recentSearchError: String? = null,
    val isLoadingSearch: Boolean = true,
    val isLoadingRecentSearches: Boolean = true,
    val searchFieldValue: String = "",
    val isLoadingItemsAndHeroes: Boolean = true,
    val allItems: List<ItemDetails> = emptyList(),
    val filteredItems: List<ItemDetails> = emptyList(),
    val itemsError: String? = null,
    val allHeroes: List<HeroDetails> = emptyList(),
    val filteredHeroes: List<HeroDetails> = emptyList(),
    val heroesError: String? = null,
    val filteredBuilds: List<BuildListItem> = emptyList(),
    val buildsError: String? = null,
    val initPlayersListText: String? = "Search a user to get started",
    val playersList: List<PlayerDetails?> = emptyList(),
    val recentSearchesList: List<PlayerDetails?> = emptyList(),
)

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val omedaCityRepository: OmedaCityRepository,
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
                async { omedaCityRepository.fetchAllItems() }

            val heroesDeferredResult =
                async { omedaCityRepository.fetchAllHeroes() }

            val recentSearches = recentSearchesDeferredResult.await()
            val itemsResult = itemsDeferredResult.await()
            val heroesResult = heroesDeferredResult.await()


            if (itemsResult.isFailure) {
                _uiState.update {
                    it.copy(
                        itemsError = itemsResult.exceptionOrNull()?.message
                    )
                }
            }

            if (heroesResult.isFailure) {
                _uiState.update {
                    it.copy(
                        heroesError = heroesResult.exceptionOrNull()?.message
                    )
                }
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isLoadingRecentSearches = false,
                    recentSearchesList = recentSearches,
                    allItems = itemsResult.getOrNull() ?: emptyList(),
                    allHeroes = heroesResult.getOrNull() ?: emptyList()
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
        _uiState.update { it.copy(isLoadingSearch = true) }
        val fieldValue = uiState.value.searchFieldValue.trim()
        val itemsList = uiState.value.allItems.filter { item ->
            item.displayName.contains(fieldValue, ignoreCase = true)
        }
        val heroesList = uiState.value.allHeroes.filter { hero ->
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
            val playersListDeferredResult = async { omedaCityRepository.fetchPlayersByName(fieldValue) }
            val buildsListDeferredResult = async { omedaCityRepository.fetchAllBuilds(
                name = fieldValue
            ) }

            val playersListResult = playersListDeferredResult.await()
            val buildsListResult = buildsListDeferredResult.await()
            if (playersListResult.isFailure) {
                _uiState.update {
                    it.copy(
                        searchError = playersListResult.exceptionOrNull()?.message
                    )
                }
            }
            if(buildsListResult.isFailure) {
                _uiState.update {
                    it.copy(
                        buildsError = buildsListResult.exceptionOrNull()?.message
                    )
                }
            }
            val filteredPlayersList = playersListResult.getOrNull()?.filter {
                !it.isCheater && !it.isMmrDisabled
            }
            _uiState.update {
                it.copy(
                    isLoadingSearch = false,
                    playersList = filteredPlayersList ?: emptyList(),
                    filteredBuilds = buildsListResult.getOrNull() ?: emptyList()
                )
            }
        }
    }
}