package com.aowen.monolith.ui.screens.builds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.network.BuildsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BuildsUiState(
    val isLoading: Boolean = true,
    val builds: List<BuildListItem> = emptyList(),
    val error: String = ""
)

@HiltViewModel
class BuildsScreenViewModel @Inject constructor(
    val repository: BuildsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BuildsUiState())
    val uiState = _uiState

    fun initViewModel() {

        viewModelScope.launch {

            val initialBuildsResponseDeferred = async { repository.getAllUserBuilds() }

            val initialBuildsResponse = initialBuildsResponseDeferred.await()


            if (initialBuildsResponse.isSuccess) {
                val initialBuilds = initialBuildsResponse.getOrNull() ?: emptyList()


                _uiState.value = BuildsUiState(
                    isLoading = false,
                    builds = initialBuilds
                )
            } else {
                _uiState.value = BuildsUiState(
                    isLoading = false,
                    error = initialBuildsResponse.exceptionOrNull()?.message ?: "Unknown error"
                )
            }
        }
    }
}