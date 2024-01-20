package com.aowen.monolith.ui.screens.builds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.network.OmedaCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BuildsUiState(
    val isLoading: Boolean = true,
    val isLoadingFilteredBuilds: Boolean = false,
    val isLoadingMoreBuilds: Boolean = false,
    val isLastPage: Boolean = false,
    val currentPage: Int = 1,
    val searchFieldValue: String = "",
    val selectedHeroFilter: Hero? = null,
    val selectedRoleFilter: HeroRole? = null,
    val selectedSortOrder: String = "Popular",
    val hasSkillOrderSelected: Boolean = false,
    val hasModulesSelected: Boolean = false,
    val builds: List<BuildListItem> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class BuildsScreenViewModel @Inject constructor(
    private val repository: OmedaCityRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<BuildsUiState> = MutableStateFlow(BuildsUiState())
    val uiState = _uiState

    init {
        initViewModel()
    }

    fun initViewModel() {
        viewModelScope.launch {
            val buildsResult = repository.fetchAllBuilds()
            if (buildsResult.isSuccess) {
                val builds = buildsResult.getOrNull() ?: emptyList()
                _uiState.update {
                    BuildsUiState(
                        isLoading = false,
                        currentPage = it.currentPage,
                        builds = builds,
                        error = null
                    )
                }
            } else {
                _uiState.update {
                    BuildsUiState(
                        isLoading = false,
                        currentPage = it.currentPage,
                        builds = it.builds,
                        error = buildsResult.exceptionOrNull()?.message
                    )
                }
            }
        }
    }

    fun filterBuilds() {

        _uiState.update {
            it.copy(
                isLoadingFilteredBuilds = true,
                currentPage = 1,
                isLastPage = false,
            )
        }
        viewModelScope.launch {
            val buildsResult = repository.fetchAllBuilds(
                name = uiState.value.searchFieldValue
                    .replace(" ", "+")
                    .trim(),
                role = uiState.value.selectedRoleFilter?.name?.lowercase(),
                order = uiState.value.selectedSortOrder.lowercase(),
                heroId = uiState.value.selectedHeroFilter?.heroId,
                skillOrder = if (uiState.value.hasSkillOrderSelected) 1 else 0,
                modules = if (uiState.value.hasModulesSelected) 1 else 0,
            )
            if (buildsResult.isSuccess) {
                val builds = buildsResult.getOrNull() ?: emptyList()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoadingFilteredBuilds = false,
                        builds = builds,
                        error = null
                    )
                }
            } else {
                _uiState.update {
                    BuildsUiState(
                        isLoading = false,
                        isLoadingFilteredBuilds = false,
                        currentPage = it.currentPage,
                        builds = it.builds,
                        selectedRoleFilter = it.selectedRoleFilter,
                        selectedHeroFilter = it.selectedHeroFilter,
                        selectedSortOrder = it.selectedSortOrder,
                        hasSkillOrderSelected = it.hasSkillOrderSelected,
                        hasModulesSelected = it.hasModulesSelected,
                        error = buildsResult.exceptionOrNull()?.message
                    )
                }
            }
        }
    }

    fun loadMoreBuilds() {
        _uiState.update {
            BuildsUiState(
                isLoading = it.isLoading,
                isLoadingMoreBuilds = true,
                isLastPage = it.isLastPage,
                currentPage = it.currentPage + 1,
                builds = it.builds,
                selectedRoleFilter = it.selectedRoleFilter,
                selectedHeroFilter = it.selectedHeroFilter,
                selectedSortOrder = it.selectedSortOrder,
                hasSkillOrderSelected = it.hasSkillOrderSelected,
                hasModulesSelected = it.hasModulesSelected,
                error = it.error
            )
        }
        viewModelScope.launch {
            val buildsResult = repository.fetchAllBuilds(
                name = uiState.value.searchFieldValue,
                role = uiState.value.selectedRoleFilter?.name?.lowercase(),
                order = uiState.value.selectedSortOrder.lowercase(),
                heroId = uiState.value.selectedHeroFilter?.heroId,
                skillOrder = if (uiState.value.hasSkillOrderSelected) 1 else null,
                modules = if (uiState.value.hasModulesSelected) 1 else null,
                page = uiState.value.currentPage
            )
            if (buildsResult.isSuccess) {
                val builds = buildsResult.getOrNull() ?: emptyList()
                _uiState.update {
                    BuildsUiState(
                        isLoading = false,
                        isLoadingMoreBuilds = false,
                        isLastPage = builds.isEmpty(),
                        currentPage = it.currentPage,
                        builds = it.builds + builds,
                        selectedRoleFilter = it.selectedRoleFilter,
                        selectedHeroFilter = it.selectedHeroFilter,
                        selectedSortOrder = it.selectedSortOrder,
                        hasSkillOrderSelected = it.hasSkillOrderSelected,
                        hasModulesSelected = it.hasModulesSelected,
                        error = null
                    )
                }
            } else {
                _uiState.update {
                    BuildsUiState(
                        isLoading = false,
                        isLoadingMoreBuilds = false,
                        isLastPage = it.isLastPage,
                        currentPage = it.currentPage,
                        builds = it.builds,
                        selectedRoleFilter = it.selectedRoleFilter,
                        selectedHeroFilter = it.selectedHeroFilter,
                        selectedSortOrder = it.selectedSortOrder,
                        hasSkillOrderSelected = it.hasSkillOrderSelected,
                        hasModulesSelected = it.hasModulesSelected,
                        error = buildsResult.exceptionOrNull()?.message
                    )
                }
            }
        }
    }

    fun updateSearchField(searchFieldValue: String) {
        _uiState.update {
            it.copy(searchFieldValue = searchFieldValue)
        }
    }

    fun updateSelectedRole(role: String) {
        _uiState.update {
            it.copy(selectedRoleFilter = HeroRole.entries.first { heroRole ->
                heroRole.name == role

            })
        }
    }

    fun clearSelectedRole() {
        _uiState.update {
            it.copy(selectedRoleFilter = null)
        }
    }

    fun updateSelectedHero(heroValue: String) {
        _uiState.update {
            it.copy(selectedHeroFilter = Hero.entries.first { hero ->
                hero.heroName == heroValue
            })
        }
    }

    fun clearSelectedHero() {
        _uiState.update {
            it.copy(selectedHeroFilter = null)
        }
    }

    fun updateSelectedSortOrder(sortOrder: String) {
        _uiState.update {
            it.copy(selectedSortOrder = sortOrder)
        }
    }

    fun clearSelectedSortOrder() {
        _uiState.update {
            it.copy(selectedSortOrder = "Popular")
        }
    }

    fun updateHasSkillOrder(hasSkillOrderSelected: Boolean) {
        _uiState.update {
            it.copy(hasSkillOrderSelected = hasSkillOrderSelected)
        }
    }

    fun updateHasModules(hasModulesSelected: Boolean) {
        _uiState.update {
            it.copy(hasModulesSelected = hasModulesSelected)
        }
    }
}