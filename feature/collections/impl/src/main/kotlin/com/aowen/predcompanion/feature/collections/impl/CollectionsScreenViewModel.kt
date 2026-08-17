package com.aowen.predcompanion.feature.collections.impl

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.repository.eternals.EternalRepository
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.model.data.HeroClass
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.ui.utils.filterOrOriginal
import com.aowen.predcompanion.feature.collections.impl.ui.data.toUiModel
import com.aowen.predcompanion.feature.collections.impl.ui.model.EternalListItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed interface HeroCollectionState {
    data object Loading : HeroCollectionState
    data class Success(val content: List<HeroDetails>) : HeroCollectionState
    data class Error(@param:StringRes val messageRes: Int) : HeroCollectionState
}

sealed interface EternalCollectionState {
    data object Loading : EternalCollectionState
    data class Success(val content: List<EternalListItemUiModel>) : EternalCollectionState
    data class Error(@param:StringRes val messageRes: Int) : EternalCollectionState
}

sealed interface ItemCollectionState {
    data object Loading : ItemCollectionState
    data class Success(val content: List<ItemDetails>) : ItemCollectionState
    data class Error(@param:StringRes val messageRes: Int) : ItemCollectionState
}

data class CollectionScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val heroesState: HeroCollectionState = HeroCollectionState.Loading,
    val eternalsState: EternalCollectionState = EternalCollectionState.Loading,
    val itemsState: ItemCollectionState = ItemCollectionState.Loading,
    val selectedRoleFilters: List<HeroRole> = emptyList(),
    val selectedStatsFilters: List<String> = emptyList(),
    val selectedHeroClass: HeroClass? = null,
    val selectedTierFilter: String? = "Tier III",
    val selectedTabIndex: Int = 0
)

@HiltViewModel
class CollectionsScreenViewModel @Inject constructor(
    omedaCityHeroRepository: HeroRepository,
    eternalRepository: EternalRepository,
    itemRepository: ItemRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionScreenUiState())
    val uiState: StateFlow<CollectionScreenUiState> = combine(
        _uiState,
        omedaCityHeroRepository.allHeroes,
        eternalRepository.allEternals,
        itemRepository.allItems
    ) { state, heroes, eternals, items ->
        val sortedHeroes = heroes.values.sortedBy { it.displayName }
        val sortedEternals = eternals.values.sortedBy { it.displayName }
        val sortedItems = items.values.sortedBy { it.displayName }
        val heroesState = when {
            sortedHeroes.isEmpty() -> HeroCollectionState.Error(messageRes = R.string.feature_collections_heroes_error)
            else -> {
                val filteredHeroes = sortedHeroes.filterOrOriginal { heroDetails ->
                    heroDetails.roles.any { role ->
                        state.selectedRoleFilters.contains(role)

                    }
                }
                HeroCollectionState.Success(filteredHeroes)
            }
        }
        val eternalsState = when {
            sortedEternals.isEmpty() -> EternalCollectionState.Error(messageRes = R.string.feature_collections_eternals_error)
            else -> EternalCollectionState.Success(sortedEternals.map { it.toUiModel() })
        }
        val itemsState = when {
            sortedItems.isEmpty() -> ItemCollectionState.Error(messageRes = R.string.feature_collections_items_error)
            else -> ItemCollectionState.Success(sortedItems.filterOrOriginal { it.matchesFilters() })
        }
        val currentHeroes =
            sortedHeroes.filter { it.roles.any(state.selectedRoleFilters::contains) }
        state.copy(
            heroesState = heroesState,
            eternalsState = eternalsState,
            itemsState = itemsState,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = CollectionScreenUiState()
    )

    fun updateRoleOption(option: HeroRole, isChecked: Boolean) {
        if (isChecked) {
            _uiState.update { it.copy(selectedRoleFilters = it.selectedRoleFilters + option) }
        } else {
            _uiState.update { it.copy(selectedRoleFilters = it.selectedRoleFilters - option) }
        }
    }

    fun onSelectHeroClass(heroClass: HeroClass) {
        _uiState.update {
            it.copy(selectedHeroClass = heroClass)
        }
    }

    fun onClearHeroClass() {
        _uiState.update {
            it.copy(selectedHeroClass = null)
        }
    }

    fun onSelectTier(tier: String) {
        _uiState.update {
            it.copy(selectedTierFilter = tier)
        }
    }

    fun onSelectStat(stat: String) {
        _uiState.update {
            if (it.selectedStatsFilters.contains(stat)) {
                it.copy(selectedStatsFilters = it.selectedStatsFilters - stat)
            } else {
                it.copy(selectedStatsFilters = it.selectedStatsFilters + stat)
            }
        }
    }

    fun onSelectTab(index: Int) {
        _uiState.update {
            it.copy(selectedTabIndex = index)
        }
    }

    private fun ItemDetails.matchesFilters(): Boolean {
        val state = _uiState.value
        val matchesClass = state.selectedHeroClass == null ||
                heroClass == state.selectedHeroClass
        val matchesTier = state.selectedTierFilter == null ||
                rarity.value == state.selectedTierFilter
        val matchesStats = state.selectedStatsFilters.isEmpty() ||
                state.selectedStatsFilters.all { stat ->
                    stats.map { it.name }.contains(stat)
                }
        return matchesClass && matchesTier && matchesStats
    }
}