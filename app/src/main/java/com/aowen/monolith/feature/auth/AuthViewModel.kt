package com.aowen.monolith.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.logDebug
import com.aowen.monolith.network.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val userId: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    var callback: (String) -> Unit = { userId ->
        _uiState.update { it.copy(userId = userId) }
    }

    fun submitLogin() {
        _uiState.update { it.copy(isLoading = true) }
        try {
            viewModelScope.launch {
                authRepo.signInWithDiscord()

            }
        } catch (e: Exception) {
            logDebug(e.toString())
        }
        _uiState.update { it.copy(isLoading = false) }
    }
}