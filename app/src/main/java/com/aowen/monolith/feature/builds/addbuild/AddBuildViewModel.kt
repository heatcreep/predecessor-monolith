package com.aowen.monolith.feature.builds.addbuild

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.AbilityDetails
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.ItemModule
import com.aowen.monolith.data.SlotType
import com.aowen.monolith.network.OmedaCityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddBuildState(
    val isLoadingHeroes: Boolean = true,
    val heroes: List<HeroDetails> = emptyList(),
    val items: List<ItemDetails> = emptyList(),
    val crestsList: List<CrestGroupDetails> = emptyList(),
    val selectedHero: HeroDetails? = null,
    val selectedRole: HeroRole? = null,
    val selectedCrest: ItemDetails? = null,
    val currentSelectedItems: PersistentList<ItemDetails> = persistentListOf(),
    val selectedStatFilters: PersistentList<String> = persistentListOf(),
    val selectedItems: PersistentList<ItemDetails> = persistentListOf(),
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
    val buildDescription: String? = null,
    val selectedSkill: AbilityDetails? = null
)

enum class CrestType {
    Support,
    Tank,
    Mage,
    Fighter,
    Sharpshooter,
    Assassin,
    Unknown
}

data class CrestGroupDetails(
    val crestType: CrestType,
    val baseCrest: ItemDetails?,
    val secondCrest: ItemDetails?,
    val finalCrests: List<ItemDetails> = emptyList()
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
            val heroesResultDeferred = async { repository.fetchAllHeroes() }
            val itemsResultDeferred = async { repository.fetchAllItems() }
            val heroesResult = heroesResultDeferred.await()
            val itemsResult = itemsResultDeferred.await()
            if (heroesResult.isSuccess && itemsResult.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoadingHeroes = false,
                    heroes = heroesResult.getOrNull() ?: emptyList(),
                    items = itemsResult.getOrNull() ?: emptyList(),
                    crestsList = groupCrests(itemsResult.getOrNull() ?: emptyList())
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoadingHeroes = false
                )
            }
        }
    }

    fun onHeroSelected(hero: HeroDetails) {
        _uiState.value = _uiState.value.copy(selectedHero = hero)
    }

    fun onRoleSelected(role: HeroRole) {
        _uiState.value = _uiState.value.copy(selectedRole = role)
    }

    fun onCrestSelected(crest: ItemDetails) {
        _uiState.value = _uiState.value.copy(selectedCrest = crest)
    }

    fun onSkillSelected(skillIndex: Int, skillId: Int) {
        _uiState.value =
            _uiState.value.copy(skillOrder = _uiState.value.skillOrder.toMutableList().apply {
                this[skillIndex] = skillId
            })
    }

    fun onSaveSkillOrder(skillOrder: List<Int>) {
        _uiState.value = _uiState.value.copy(skillOrder = skillOrder)
    }

    fun onSkillDetailsSelected(skill: AbilityDetails) {
        _uiState.value = _uiState.value.copy(selectedSkill = skill)
    }

    fun onChangeModuleOrder(fromIndex: Int, toIndex: Int) {
        val newModuleOrder = _uiState.value.modules.toMutableList().apply {
            val module = removeAt(fromIndex)
            add(toIndex, module)
        }
        _uiState.value = _uiState.value.copy(modules = newModuleOrder)
    }

    fun onSelectStatFilter(stat: String) {
        _uiState.value = _uiState.value.copy(
            selectedStatFilters = if(uiState.value.selectedStatFilters.contains(stat)) {
                _uiState.value.selectedStatFilters.remove(stat)
            } else {
                _uiState.value.selectedStatFilters.add(stat)
            }
        )
    }

    fun onClearStatFilters() {
        _uiState.value = _uiState.value.copy(
            selectedStatFilters = persistentListOf()
        )
    }

    fun onAddSelectedItem(item: ItemDetails) {
        _uiState.value = _uiState.value.copy(
            selectedItems = _uiState.value.selectedItems.add(item)
        )
    }

    fun onReplaceSelectedItem(newItem: ItemDetails, itemPosition: Int) {
        val newItemOrder = _uiState.value.selectedItems.toMutableList().apply {
            removeAt(itemPosition)
            add(itemPosition,newItem)
        }.toPersistentList()
        _uiState.value = _uiState.value.copy(selectedItems = newItemOrder)
    }

    fun onRemoveSelectedItem(item: ItemDetails) {
        _uiState.value = _uiState.value.copy(
            selectedItems = _uiState.value.selectedItems.remove(item)
        )
    }

    fun onChangeItemOrder(fromIndex: Int, toIndex: Int) {
        val newItemOrder = _uiState.value.selectedItems.toMutableList().apply {
            val item = removeAt(fromIndex)
            add(toIndex, item)
        }.toPersistentList()
        _uiState.value = _uiState.value.copy(selectedItems = newItemOrder)
    }

    fun onCreateNewModule(title: String, itemIds: List<Int>) {
        _uiState.value = _uiState.value.copy(
            modules = _uiState.value.modules.plus(
                ItemModule(
                    title = title,
                    items = itemIds
                )
            )
        )
    }

    fun onBuildTitleChanged(title: String) {
        _uiState.value = _uiState.value.copy(buildTitle = title)
    }

    fun onBuildDescriptionChanged(description: String) {
        _uiState.value = _uiState.value.copy(buildDescription = description)
    }

    private fun groupCrests(items: List<ItemDetails>): List<CrestGroupDetails> {
        val crestDetailsList = mutableListOf<CrestGroupDetails>()

        val allCrests = items.filter { it.slotType == SlotType.CREST }
        val baseCrests = allCrests.filter { it.requirements.isEmpty() }

        baseCrests.forEach { baseCrest ->
            val secondCrest = allCrests.first { it.requirements.contains(baseCrest.name) }
            val finalCrests = allCrests.filter { it.requirements.contains(secondCrest.name)}
            crestDetailsList.add(
                CrestGroupDetails(
                    crestType = CrestType.valueOf(baseCrest.heroClass ?: "Unknown"),
                    baseCrest = baseCrest,
                    secondCrest = secondCrest,
                    finalCrests = finalCrests,
                )
            )
        }

        return crestDetailsList
    }
}