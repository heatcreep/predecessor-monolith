package com.aowen.monolith.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.PlayerDetails
import com.aowen.monolith.data.PlayerStats
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.user.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MonolithUiState(
    val isLoading: Boolean = true,
    val session: UserSession? = null,
    val claimedPlayerStats: PlayerStats? = null,
    val claimedPlayerDetails: PlayerDetails? = null,
)

@HiltViewModel
class MonolithViewModel @Inject constructor(
    private val supabaseClient: SupabaseClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(MonolithUiState())
    val uiState: StateFlow<MonolithUiState> = _uiState

    fun setClaimedPlayer(playerStats: PlayerStats, playerDetails: PlayerDetails) {
        _uiState.update {
            it.copy(
                claimedPlayerStats = playerStats,
                claimedPlayerDetails = playerDetails
            )
        }
    }

    init {
        viewModelScope.launch {
            val session = supabaseClient.gotrue.currentSessionOrNull()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    session = session
                )
            }
        }
    }
}