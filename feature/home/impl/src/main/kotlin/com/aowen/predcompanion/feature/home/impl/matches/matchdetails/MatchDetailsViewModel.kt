package com.aowen.predcompanion.feature.home.impl.matches.matchdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.repository.matches.MatchRepository
import com.aowen.predcompanion.core.network.getOrThrow
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchDetailsUiModel
import com.aowen.predcompanion.feature.home.impl.matches.model.MatchTeamUiModel
import com.aowen.predcompanion.feature.home.impl.matches.model.mapper.MatchDetailsUiMapper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MatchDetailsErrors(
    val errorMessage: String? = "Failed to fetch match details.",
)

sealed interface MatchDetailsUiState {
    data object Loading : MatchDetailsUiState
    data class Error(val matchDetailsErrors: MatchDetailsErrors) : MatchDetailsUiState
    data class Success(
        val match: MatchDetailsUiModel,
        val selectedTeam: MatchTeamUiModel,
    ) : MatchDetailsUiState
}

@HiltViewModel(assistedFactory = MatchDetailsViewModel.Factory::class)
class MatchDetailsViewModel @AssistedInject constructor(
    @Assisted private val matchId: String,
    private val matchRepository: MatchRepository,
    private val matchDetailsUiMapper: MatchDetailsUiMapper
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted matchId: String): MatchDetailsViewModel
    }

    private val _matchDetails = MutableStateFlow<MatchDetailsUiModel?>(null)
    private val _selectedTeamIsDusk = MutableStateFlow(false)
    private val _error = MutableStateFlow<MatchDetailsErrors?>(null)

    val uiState: StateFlow<MatchDetailsUiState> = combine(
        _matchDetails,
        _selectedTeamIsDusk,
        _error,
    ) { match, isDusk, error ->
        when {
            error != null -> MatchDetailsUiState.Error(error)
            match != null -> MatchDetailsUiState.Success(
                match = match,
                selectedTeam = if (isDusk) match.duskTeam else match.dawnTeam,
            )
            else -> MatchDetailsUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MatchDetailsUiState.Loading,
    )

    init {
        initViewModel()
    }

    fun initViewModel() {
        _error.value = null
        _matchDetails.value = null
        _selectedTeamIsDusk.value = false

        viewModelScope.launch {
            try {
                val match = matchRepository.fetchMatchById(matchId).getOrThrow()
                if (match == null) {
                    _error.value = MatchDetailsErrors(errorMessage = "Match not found.")
                    return@launch
                }
                _matchDetails.value = matchDetailsUiMapper.invoke(match)
            } catch (e: Exception) {
                _error.value = MatchDetailsErrors(errorMessage = e.message)
            }
        }
    }

    fun onTeamSelected(duskTeamSelected: Boolean) {
        _selectedTeamIsDusk.value = duskTeamSelected
    }
}
