package com.aowen.monolith.feature.builds.addbuild

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.AbilityDetails
import com.aowen.monolith.data.Console
import com.aowen.monolith.data.HeroDetails
import com.aowen.monolith.data.HeroRole
import com.aowen.monolith.data.ItemDetails
import com.aowen.monolith.data.ItemModule
import com.aowen.monolith.data.SlotType
import com.aowen.monolith.data.repository.heroes.HeroRepository
import com.aowen.monolith.data.repository.items.ItemRepository
import com.aowen.monolith.network.OmedaCityRepository
import com.aowen.monolith.network.Resource
import com.aowen.monolith.network.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ModuleState {
    data object Create : ModuleState
    data object Edit : ModuleState
}

data class AddBuildState(
    val isLoadingHeroes: Boolean = true,
    val heroes: List<HeroDetails> = emptyList(),
    val items: List<ItemDetails> = emptyList(),
    val crestsList: List<CrestGroupDetails> = emptyList(),
    val selectedHero: HeroDetails? = null,
    val selectedRole: HeroRole = HeroRole.Unknown,
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
    val workingModule: ItemModule = ItemModule(),
    val buildTitle: String = "",
    val buildDescription: String = "",
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
    val repository: OmedaCityRepository,
    val omedaCityHeroRepository: HeroRepository,
    val omedaCityItemRepository: ItemRepository,
    val userPreferencesDataStore: UserPreferencesManager
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddBuildState> = MutableStateFlow(AddBuildState())
    val uiState = _uiState

    private val _console = MutableStateFlow(Console.PC)
    val console = _console

    init {
        initViewModel()
    }

    fun initViewModel() {
        viewModelScope.launch {
            _console.emit(userPreferencesDataStore.console.first())
            val heroesResultDeferred = async { omedaCityHeroRepository.fetchAllHeroes() }
            val itemsResultDeferred = async { omedaCityItemRepository.fetchAllItems() }
            val heroesResult = heroesResultDeferred.await()
            val itemsResult = itemsResultDeferred.await()
            if (heroesResult is Resource.Success && itemsResult is Resource.Success) {
                _uiState.value = _uiState.value.copy(
                    isLoadingHeroes = false,
                    heroes = heroesResult.data,
                    items = itemsResult.data,
                    crestsList = groupCrests(itemsResult.data)
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
            selectedStatFilters = if (uiState.value.selectedStatFilters.contains(stat)) {
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
            add(itemPosition, newItem)
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

    fun initWorkingModule(itemModule: ItemModule) {
        _uiState.value = _uiState.value.copy(
            workingModule = itemModule
        )
    }

    fun onChangeWorkingModuleTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            workingModule = uiState.value.workingModule.copy(title = title)
        )
    }

    fun onAddItemToWorkingModule(itemId: Int) {
        _uiState.value = _uiState.value.copy(
            workingModule = _uiState.value.workingModule.copy(
                items = uiState.value.workingModule.items.plus(itemId)
            )
        )
    }

    fun onReplaceItemInWorkingModule(itemDetails: ItemDetails, itemPosition: Int) {
        val newItemOrder = _uiState.value.workingModule.items.toMutableList().apply {
            removeAt(itemPosition)
            add(itemPosition, itemDetails.id)
        }.toPersistentList()
        _uiState.value = _uiState.value.copy(
            workingModule = uiState.value.workingModule.copy(items = newItemOrder)
        )
    }

    fun clearWorkingModule() {
        _uiState.value = _uiState.value.copy(
            workingModule = ItemModule()
        )
    }

    fun onCreateNewModule() {
        val isExistingModule = uiState.value.modules.any { it.id == uiState.value.workingModule.id }
        _uiState.value = if (isExistingModule) {
            _uiState.value.copy(
                modules = uiState.value.modules.toMutableList().apply {
                    val index = indexOfFirst { it.id == uiState.value.workingModule.id }
                    removeAt(index)
                    add(index, uiState.value.workingModule)
                }
            )
        } else {
            _uiState.value.copy(
                modules = uiState.value.modules.plus(uiState.value.workingModule)
            )
        }
    }

    fun onChangeModuleItemOrder(fromIndex: Int, toIndex: Int) {
        val selectedModule = uiState.value.workingModule
        val newItemOrder = selectedModule.items.toMutableList().apply {
            val item = removeAt(fromIndex)
            add(toIndex, item)
        }.toPersistentList()
        _uiState.value =
            _uiState.value.copy(workingModule = selectedModule.copy(items = newItemOrder))
    }

    fun onBuildTitleChanged(title: String) {
        _uiState.value = _uiState.value.copy(buildTitle = title)
    }

    fun onBuildDescriptionChanged(description: String) {
        _uiState.value = _uiState.value.copy(buildDescription = description)
    }

    fun onSubmitNewBuild(): String {
        return parseNewBuildUrl()
    }

    private fun parseNewBuildUrl(): String {
        val itemIdMap = mutableMapOf<String, Int>()
        val skillOrderMap = mutableMapOf<String, Int>()
        val modulesMap = mutableMapOf<String, String>()

        // Add item ids to map
        uiState.value.selectedItems.mapIndexed { index, item ->
            itemIdMap["build%5Bitem${index + 1}_id%5D"] = item.id
        }

        // Add skill order to map
        uiState.value.skillOrder.mapIndexed { index, order ->
            skillOrderMap["build[skill_order][${index + 1}]"] = order
        }

        // Add modules to map
        uiState.value.modules.mapIndexed { index, itemModule ->
            modulesMap["build[modules_attributes][$index][title]"] = itemModule.title
            itemModule.items.mapIndexed { itemIndex, itemId ->
                modulesMap["build[modules_attributes][$index][item${itemIndex + 1}_id]"] =
                    itemId.toString()
            }
        }

        return "https://omeda.city/builds/new?build[title]=${uiState.value.buildTitle}&" +
                "build[description]=${uiState.value.buildDescription}&" +
                "build[role]=${uiState.value.selectedRole.roleName}&" +
                "build[hero_id]=${uiState.value.selectedHero?.id ?: -1}&" +
                "build[crest_id]=${uiState.value.selectedCrest?.id ?: -1}&" +
                itemIdMap.map { "${it.key}=${it.value}" }.joinToString("&") + "&" +
                skillOrderMap.map { "${it.key}=${it.value}" }.joinToString("&") + "&" +
                modulesMap.map { "${it.key}=${it.value}" }.joinToString("&")
    }

    private fun groupCrests(items: List<ItemDetails>): List<CrestGroupDetails> {
        val crestDetailsList = mutableListOf<CrestGroupDetails>()

        val allCrests = items.filter { it.slotType == SlotType.CREST }
        val baseCrests = allCrests.filter { it.requirements.isEmpty() }

        baseCrests.forEach { baseCrest ->
            val secondCrest = allCrests.first { it.requirements.contains(baseCrest.name) }
            val finalCrests = allCrests.filter { it.requirements.contains(secondCrest.name) }
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