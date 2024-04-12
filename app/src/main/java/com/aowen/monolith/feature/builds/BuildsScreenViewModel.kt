package com.aowen.monolith.feature.builds

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.aowen.monolith.data.Hero
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.network.OmedaCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class BuildsUiState(
    val searchFieldValue: String = "",
    val selectedHeroFilter: Hero? = null,
    val selectedRoleFilter: HeroRole? = null,
    val selectedSortOrder: String = "Popular",
    val hasSkillOrderSelected: Boolean = false,
    val hasModulesSelected: Boolean = false,
    val hasCurrentVersionSelected: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class BuildsScreenViewModel @Inject constructor(
    private val repository: OmedaCityRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<BuildsUiState> = MutableStateFlow(BuildsUiState())
    val uiState = _uiState

    lateinit var buildsPagingSource: BuildsPagingSource

    val buildsPager = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE)
    ) {
        BuildsPagingSource(
            name = uiState.value.searchFieldValue
                .replace(" ", "+")
                .trim(),
            role = uiState.value.selectedRoleFilter?.name?.lowercase(),
            order = uiState.value.selectedSortOrder.lowercase(),
            heroId = uiState.value.selectedHeroFilter?.heroId,
            skillOrder = if (uiState.value.hasSkillOrderSelected) 1 else null,
            modules = if (uiState.value.hasModulesSelected) 1 else null,
            currentVersion = if(uiState.value.hasCurrentVersionSelected) 1 else null,
            repository = repository
        ).also {
            buildsPagingSource = it
        }
    }.flow

    fun updateSearchField(searchFieldValue: String) {
        _uiState.update {
            it.copy(searchFieldValue = searchFieldValue)
        }
        buildsPagingSource.invalidate()
    }

    fun updateSelectedRole(role: String) {
        _uiState.update {
            it.copy(selectedRoleFilter = HeroRole.entries.first { heroRole ->
                heroRole.name == role

            })
        }
        buildsPagingSource.invalidate()
    }

    fun clearSelectedRole() {
        _uiState.update {
            it.copy(selectedRoleFilter = null)
        }
        buildsPagingSource.invalidate()
    }

    fun updateSelectedHero(heroValue: String) {
        _uiState.update {
            it.copy(selectedHeroFilter = Hero.entries.first { hero ->
                hero.heroName == heroValue
            })
        }
        buildsPagingSource.invalidate()
    }

    fun clearSelectedHero() {
        _uiState.update {
            it.copy(selectedHeroFilter = null)
        }
        buildsPagingSource.invalidate()
    }

    fun updateSelectedSortOrder(sortOrder: String) {
        _uiState.update {
            it.copy(selectedSortOrder = sortOrder)
        }
        buildsPagingSource.invalidate()
    }

    fun clearSelectedSortOrder() {
        _uiState.update {
            it.copy(selectedSortOrder = "Popular")
        }
        buildsPagingSource.invalidate()
    }

    fun updateHasSkillOrder(hasSkillOrderSelected: Boolean) {
        _uiState.update {
            it.copy(hasSkillOrderSelected = hasSkillOrderSelected)
        }
        buildsPagingSource.invalidate()
    }

    fun updateHasModules(hasModulesSelected: Boolean) {
        _uiState.update {
            it.copy(hasModulesSelected = hasModulesSelected)
        }
        buildsPagingSource.invalidate()
    }

    fun updateHasCurrentVersion(hasCurrentVersionSelected: Boolean) {
        _uiState.update {
            it.copy(hasCurrentVersionSelected = hasCurrentVersionSelected)
        }
        buildsPagingSource.invalidate()
    }
}