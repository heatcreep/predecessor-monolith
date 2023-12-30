package com.aowen.monolith.ui.screens.builds.builddetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.network.BuildsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BuildDetailsUiState(
    val isLoading: Boolean = true,
    val error: String = ""
)

class BuildDetailsScreenViewModel @Inject constructor(
    val repository: BuildsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BuildDetailsUiState())
    val uiState = _uiState

    fun initViewModel() {

        viewModelScope.launch {

            val initialBuildsResponseDeferred = async { repository.getAllUserBuilds() }

            val initialBuildsResponse = initialBuildsResponseDeferred.await()
        }
    }
}