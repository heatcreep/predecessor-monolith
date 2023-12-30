package com.aowen.monolith.ui.screens.builds.addbuild

import androidx.lifecycle.ViewModel
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.ItemDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

data class AddBuildState(
    val selectedHero: Int? = null,
    val selectedRole: HeroRole? = null,
    val selectedCrest: ItemDetails? = null,
    val selectedItems: List<ItemDetails> = emptyList(),
    val skillOrder: List<Int> = listOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    val firstBuyItem: ItemDetails? = null,
    val selectedCoreItems: List<ItemDetails> = emptyList(),
    val selectedFlexItems: List<ItemDetails> = emptyList(),
    val buildDescription: String? = null
)

@HiltViewModel
class AddBuildViewModel @Inject constructor(): ViewModel() {

    private val _uiState: MutableStateFlow<AddBuildState> = MutableStateFlow(AddBuildState())
    val uiState = _uiState

    fun onHeroSelected(heroId: Int) {
        _uiState.value = _uiState.value.copy(selectedHero = heroId)
    }

    fun onRoleSelected(role: HeroRole) {
        _uiState.value = _uiState.value.copy(selectedRole = role)
    }

    fun onCrestSelected(crest: ItemDetails) {
        _uiState.value = _uiState.value.copy(selectedCrest = crest)
    }

    fun onSkillSelected(skillIndex: Int, skillId: Int) {
        _uiState.value = _uiState.value.copy(skillOrder = _uiState.value.skillOrder.toMutableList().apply {
            this[skillIndex] = skillId
        })
    }

    fun onItemAdded(item: ItemDetails) {
        _uiState.value = _uiState.value.copy(selectedItems = _uiState.value.selectedItems + item)
    }

    fun onFirstBuyItemSelected(item: ItemDetails) {
        _uiState.value = _uiState.value.copy(firstBuyItem = item)
    }

    fun onCoreItemSelected(item: ItemDetails) {
        _uiState.value =
            _uiState.value.copy(selectedCoreItems = _uiState.value.selectedCoreItems + item)
    }

    fun onFlexItemSelected(item: ItemDetails) {
        _uiState.value =
            _uiState.value.copy(selectedFlexItems = _uiState.value.selectedFlexItems + item)
    }

    fun onBuildDescriptionChanged(description: String) {
        _uiState.value = _uiState.value.copy(buildDescription = description)
    }
}