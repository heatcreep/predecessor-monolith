package com.aowen.monolith.ui.screens.builds.addbuild

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.ItemModule
import com.aowen.monolith.network.OmedaCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddBuildState(
    val isLoadingHeroes: Boolean = true,
    val heroes: List<HeroDetails> = emptyList(),
    val selectedHeroId: Int? = null,
    val selectedRole: HeroRole? = null,
    val selectedCrestId: Int? = null,
    val selectedItems: List<Int> = emptyList(),
    val skillOrder: List<Int> = listOf(
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1
    ),
    val modules: List<ItemModule> = emptyList(),
    val buildTitle: String = "",
    val buildDescription: String? = null
)

@HiltViewModel
class AddBuildViewModel @Inject constructor(
    val repository: OmedaCityRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddBuildState> = MutableStateFlow(AddBuildState())
    val uiState = _uiState

    init {
        initViewModel()
    }

    fun initViewModel() {
        viewModelScope.launch {
            val heroesResult = repository.fetchAllHeroes()
            if(heroesResult.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoadingHeroes = false,
                    heroes = heroesResult.getOrNull() ?: emptyList()
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoadingHeroes = false
                )
            }
        }
    }

    fun onHeroSelected(hero: HeroDetails) {
        _uiState.value = _uiState.value.copy(selectedHeroId = hero.id)
    }

    fun onRoleSelected(role: HeroRole) {
        _uiState.value = _uiState.value.copy(selectedRole = role)
    }

    fun onCrestSelected(crest: ItemDetails) {
        _uiState.value = _uiState.value.copy(selectedCrestId = crest.id)
    }

    fun onSkillSelected(skillIndex: Int, skillId: Int) {
        _uiState.value =
            _uiState.value.copy(skillOrder = _uiState.value.skillOrder.toMutableList().apply {
                this[skillIndex] = skillId
            })
    }

    fun onItemAdded(itemId: Int) {
        _uiState.value = _uiState.value.copy(selectedItems = _uiState.value.selectedItems + itemId)
    }

    fun onChangeModuleOrder(fromIndex: Int, toIndex: Int) {
        val newModuleOrder = _uiState.value.modules.toMutableList().apply {
            val module = removeAt(fromIndex)
            add(toIndex, module)
        }
        _uiState.value = _uiState.value.copy(modules = newModuleOrder)
    }

    fun onChangeItemOrder(fromIndex: Int, toIndex: Int) {
        val newItemOrder = _uiState.value.selectedItems.toMutableList().apply {
            val item = removeAt(fromIndex)
            add(toIndex, item)
        }
        _uiState.value = _uiState.value.copy(selectedItems = newItemOrder)
    }

    fun onCreateNewModule(title: String, itemIds: List<Int>) {
        _uiState.value = _uiState.value.copy(modules = _uiState.value.modules.plus(ItemModule(
            title = title,
            items = itemIds
        )))
    }

    fun onBuildTitleChanged(title: String) {
        _uiState.value = _uiState.value.copy(buildTitle = title)
    }

    fun onBuildDescriptionChanged(description: String) {
        _uiState.value = _uiState.value.copy(buildDescription = description)
    }
}