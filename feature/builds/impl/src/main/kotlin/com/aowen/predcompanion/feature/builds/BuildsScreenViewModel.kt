package com.aowen.predcompanion.feature.builds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.aowen.predcompanion.core.data.model.HeroUiModel
import com.aowen.predcompanion.core.data.repository.builds.BuildRepository
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.ui.model.mapper.BuildListItemUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HeroDropDownUiModel(
    val id: Long,
    val name: String,
    val imageSrc: String? = null
)

data class BuildsUiState(
    val allHeroes: List<HeroUiModel> = emptyList(),
    val searchFieldValue: String = "",
    val selectedHeroFilter: HeroDropDownUiModel? = null,
    val selectedRoleFilter: HeroRole? = null,
    val selectedSortOrder: String = "Popular",
    val hasSkillOrderSelected: Boolean = false,
    val hasModulesSelected: Boolean = false,
    val hasCurrentVersionSelected: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class BuildsScreenViewModel @Inject constructor(
    private val omedaCityBuildRepository: BuildRepository,
    private val omedaCityHeroRepository: HeroRepository,
    private val buildListItemUiMapper: BuildListItemUiMapper
) : ViewModel() {

    private val _uiState: MutableStateFlow<BuildsUiState> = MutableStateFlow(BuildsUiState())
    val uiState = _uiState

    lateinit var buildsPagingSource: BuildsPagingSource

    init {
        viewModelScope.launch {
            val allHeroes = omedaCityHeroRepository.getAllHeroes().map {
                HeroUiModel(
                    heroId = it.id,
                    name = it.displayName,
                    imageSrc = it.imageUrl
                )
            }
            _uiState.update {
                it.copy(allHeroes = allHeroes)
            }
        }
    }

    val buildsPager = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE)
    ) {
        BuildsPagingSource(
            name = uiState.value.searchFieldValue
                .replace(" ", "+")
                .trim(),
            role = uiState.value.selectedRoleFilter?.name?.lowercase(),
            order = uiState.value.selectedSortOrder.lowercase(),
            heroId = uiState.value.selectedHeroFilter?.id,
            skillOrder = if (uiState.value.hasSkillOrderSelected) 1 else null,
            modules = if (uiState.value.hasModulesSelected) 1 else null,
            currentVersion = if (uiState.value.hasCurrentVersionSelected) 1 else null,
            omedaCityBuildRepository = omedaCityBuildRepository,
            buildListItemUiMapper = buildListItemUiMapper

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

    fun updateSelectedRole(role: HeroRole) {
        _uiState.update {
            it.copy(selectedRoleFilter = role)
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
            it.copy(selectedHeroFilter = omedaCityHeroRepository.getHeroByName(heroValue)?.let {
                HeroDropDownUiModel(id = it.id, name = it.displayName, imageSrc = it.imageUrl)
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