package com.aowen.monolith.ui.screens.builds

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

data class Build(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val items: List<Int>
)

data class BuildsUiState(
    val isLoading: Boolean = true,
    val builds: List<Build> = emptyList(),
    val error: String = ""
)

@HiltViewModel
class BuildsScreenViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(BuildsUiState())
    val uiState = _uiState

    fun initViewModel() {
        _uiState.value = BuildsUiState(isLoading = false)
    }
}