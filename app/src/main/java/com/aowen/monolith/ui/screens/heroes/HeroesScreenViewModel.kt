package com.aowen.monolith.ui.screens.heroes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.ui.utils.filterOrOriginal
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

    init {
        initViewModel()
    }

    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {

            val heroesResult = repository.fetchAllHeroes()
            if (heroesResult.isSuccess) {
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