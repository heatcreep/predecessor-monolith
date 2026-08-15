package com.aowen.predcompanion.feature.collections.impl

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

data class HeroesScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val allHeroes: List<HeroDetails> = emptyList(),
    val allEternals: List<EternalListItemUiModel> = emptyList(),
    val allItems: List<ItemDetails> = emptyList(),
    val currentHeroes: List<HeroDetails> = emptyList(),
    val selectedRoleFilters: List<HeroRole> = emptyList(),
    val selectedStatsFilters: List<String> = emptyList(),
    val selectedHeroClass: HeroClass? = null,
    val selectedTierFilter: String? = "Tier III",
    val searchFieldValue: String = "",
    val selectedTabIndex: Int = 0
)

@HiltViewModel
class HeroesScreenViewModel @Inject constructor(
    omedaCityHeroRepository: HeroRepository,
    eternalRepository: EternalRepository,
    itemRepository: ItemRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HeroesScreenUiState())
    val uiState: StateFlow<HeroesScreenUiState> = combine(
        _uiState,
        omedaCityHeroRepository.allHeroes,
        eternalRepository.allEternals,
        itemRepository.allItems
    ) { state, heroes, eternals, items ->
        val sortedHeroes = heroes.values.sortedBy { it.displayName }
        val currentHeroes =
            sortedHeroes.filter { it.roles.any(state.selectedRoleFilters::contains) }
        state.copy(
            allHeroes = sortedHeroes,
            allEternals = eternals.values
                .sortedBy { it.displayName }
                .map { it.toUiModel() },
            allItems = items.values
                .filter { item ->
                    val matchesClass = state.selectedHeroClass == null ||
                            item.heroClass == state.selectedHeroClass
                    val matchesTier = state.selectedTierFilter == null ||
                            item.rarity.value == state.selectedTierFilter
                    val matchesStats = state.selectedStatsFilters.isEmpty() ||
                            state.selectedStatsFilters.all { stat ->
                                item.stats.map { it.name }.contains(stat)
                            }
                    matchesClass && matchesTier && matchesStats
                },
            currentHeroes = currentHeroes.ifEmpty { sortedHeroes },
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HeroesScreenUiState()
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

    fun getFilteredHeroes() {
        // get heroes by role and return the original list if no roles are selected
        val heroesByRole =
            uiState.value.allHeroes.filterOrOriginal { heroDetails ->
                heroDetails.roles.any { role ->
                    uiState.value.selectedRoleFilters.contains(role)

                }
            }

        // get heroes by search
        val heroesBySearch = heroesByRole.filter { heroDetails ->
            heroDetails.displayName.contains(uiState.value.searchFieldValue, ignoreCase = true)
        }

        _uiState.update {
            it.copy(currentHeroes = heroesBySearch)
        }
    }
}