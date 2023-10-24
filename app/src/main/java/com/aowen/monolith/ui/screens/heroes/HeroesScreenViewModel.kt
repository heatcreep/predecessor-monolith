package com.aowen.monolith.ui.screens.heroes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.network.OmedaCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HeroesScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val allHeroes: List<HeroDetails> = emptyList(),
    val currentHeroes: List<HeroDetails> = emptyList(),
    var selectedRoleFilters: List<HeroRole> = emptyList(),
    val searchFieldValue: String = ""
)

@HiltViewModel
class HeroesScreenViewModel @Inject constructor(
    private val repository: OmedaCityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HeroesScreenUiState())
    val uiState: StateFlow<HeroesScreenUiState> = _uiState


    fun setSearchValue(text: String) {
        _uiState.update {
            it.copy(
                searchFieldValue = text.trim()
            )
        }
    }

    fun updateRoleOption(option: HeroRole, isChecked: Boolean) {
        if (isChecked) {
            _uiState.update { it.copy(selectedRoleFilters = it.selectedRoleFilters + option) }
        } else {
            _uiState.update { it.copy(selectedRoleFilters = it.selectedRoleFilters - option) }
        }
    }

    fun getFilteredHeroes() {
        // if both role filters and search field are empty, return all heroes
        val currentHeroes =
            if (uiState.value.selectedRoleFilters.isEmpty() && uiState.value.searchFieldValue.isEmpty()) {
                uiState.value.allHeroes
                // if search field is empty, filter by role filters
            } else if (uiState.value.searchFieldValue.isEmpty()) {
                uiState.value.allHeroes.filter { hero ->
                    hero.roles.any { role ->
                        uiState.value.selectedRoleFilters.contains(role)
                    }
                }
                // if role filters are empty, filter by search field
            } else if (uiState.value.selectedRoleFilters.isEmpty()) {
                uiState.value.allHeroes.filter { hero ->
                    hero.displayName.contains(uiState.value.searchFieldValue, ignoreCase = true)
                }
                // if both role filters and search field are not empty, filter by both
            } else {
                uiState.value.allHeroes.filter { hero ->
                    hero.roles.any { role ->
                        uiState.value.selectedRoleFilters.contains(role)
                    }
                }.filter { hero ->
                    hero.displayName.contains(uiState.value.searchFieldValue, ignoreCase = true)
                }
            }

        _uiState.update { it.copy(currentHeroes = currentHeroes) }
    }

    init {
        initViewModel()
    }

    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {

            val heroesResult = repository.fetchAllHeroes()
            if(heroesResult.isSuccess) {
                val heroes = heroesResult.getOrNull() ?: emptyList()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        allHeroes = heroes,
                        currentHeroes = heroes
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to fetch heroes."
                    )
                }
            }
        }
    }
}