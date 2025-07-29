package com.aowen.monolith.feature.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.HeroClass
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.repository.items.ItemRepository
import com.aowen.monolith.network.getOrThrow
import com.aowen.monolith.ui.utils.filterOrOriginal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

val defaultStats = listOf(
    "Max Health",
    "Max Mana",
    "Health Regen",
    "Mana Regen",
    "Physical Power",
    "Magical Power",
    "Attack Speed",
    "Physical Armor",
    "Magical Armor",
    "Heal and Shield Increase",
    "Ability Haste",
    "Lifesteal",
    "Magical Lifesteal",
    "Omnivamp",
    "Movement Speed",
    "Physical Penetration",
    "Magical Penetration",
    "Critical Chance",
    "Tenacity",
)

data class ItemsUiState(
    val isLoading: Boolean = true,
    val searchFieldValue: String = "",
    val selectedTierFilter: String? = "Tier III",
    val selectedHeroClass: HeroClass? = null,
    val allItems: List<ItemDetails> = emptyList(),
    val filteredItems: List<ItemDetails> = emptyList(),
    val allStats: List<String> = defaultStats,
    val selectedStatFilters: List<String> = emptyList(),
    val itemsError: String? = null
)

@HiltViewModel
class ItemsViewModel @Inject constructor(
    val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ItemsUiState())
    val uiState: StateFlow<ItemsUiState> = _uiState

    init {
        initViewModel()
    }

    private fun initViewModel() {
        viewModelScope.launch {
            try {
                val allItems = itemRepository.fetchAllItems().getOrThrow().sortedBy { it.displayName }
                val sortedItems = allItems.filterOrOriginal {
                    it.rarity.value == uiState.value.selectedTierFilter
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        allItems = allItems,
                        filteredItems = sortedItems,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        itemsError = e.message
                    )
                }
            }
        }
    }

    fun onSetSearchValue(value: String) {
        _uiState.update {
            it.copy(searchFieldValue = value)
        }
    }

    fun onSelectTier(tier: String) {
        _uiState.update {
            it.copy(selectedTierFilter = tier)
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

    fun onClearTier() {
        _uiState.update {
            it.copy(selectedTierFilter = null)
        }
    }

    fun onSelectStat(stat: String) {
        _uiState.update {
            if (it.selectedStatFilters.contains(stat)) {
                it.copy(selectedStatFilters = it.selectedStatFilters - stat)
            } else {
                it.copy(selectedStatFilters = it.selectedStatFilters + stat)
            }
        }
    }

    fun onClearStats() {
        _uiState.update {
            it.copy(selectedStatFilters = emptyList())
        }
    }

    fun onClearSearch() {
        _uiState.update {
            it.copy(searchFieldValue = "")
        }
    }

    fun getFilteredItems() {

        val itemsByHeroClass = uiState.value.allItems.filterOrOriginal {
            it.heroClass == uiState.value.selectedHeroClass
        }
        val itemsByTier =
            itemsByHeroClass.filterOrOriginal {
                it.rarity.value == uiState.value.selectedTierFilter
            }


        val itemsByStats = itemsByTier.filter { item ->
            uiState.value.selectedStatFilters.all { listItem ->
                item.stats.map { it.name }.contains(listItem)
            }
        }

        val itemsBySearch = itemsByStats.filter { item ->
            item.displayName.contains(uiState.value.searchFieldValue, ignoreCase = true)
        }

        _uiState.update {
            it.copy(filteredItems = itemsBySearch)
        }
    }
}
