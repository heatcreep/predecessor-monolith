package com.aowen.predcompanion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.repository.eternals.EternalRepository
import com.aowen.predcompanion.core.data.repository.heroes.HeroRepository
import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AppDataState {
    data class Loading(val message: String) : AppDataState
    data object Ready : AppDataState
    data class Error(val message: String) : AppDataState
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val heroRepository: HeroRepository,
    private val eternalRepository: EternalRepository
) : ViewModel() {

    private val _appDataState = MutableStateFlow<AppDataState>(AppDataState.Loading("Items"))
    val appDataState: StateFlow<AppDataState> = _appDataState

    fun loadAppData() {
        viewModelScope.launch {
            // Get all items
            _appDataState.value = AppDataState.Loading("Items")
            itemRepository.fetchAllItems()
            if (itemRepository.allItems.value.isEmpty()) {
                _appDataState.value = AppDataState.Error("Failed to load items")
                return@launch
            }

            // Get all heroes
            _appDataState.value = AppDataState.Loading("Heroes")
            heroRepository.fetchAllHeroes()
            if (heroRepository.allHeroes.value.isEmpty()) {
                _appDataState.value = AppDataState.Error("Failed to load heroes")
                return@launch
            }

            // Get all eternals
            _appDataState.value = AppDataState.Loading("Eternals")
            eternalRepository.fetchAllEternals()
            if (eternalRepository.allEternals.value.isEmpty()) {
                _appDataState.value = AppDataState.Error("Failed to load eternals")
                return@launch
            }

            _appDataState.value = AppDataState.Ready
        }
    }
}