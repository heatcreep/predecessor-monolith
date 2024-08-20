package com.aowen.monolith.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.logDebug
import com.aowen.monolith.network.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

abstract class UserState {
    object Unauthenticated : UserState()
    data class Authenticated(val accessToken: String) : UserState()
}

data class LoginUiState(
    val isLoading: Boolean = true,
    val userState: UserState = UserState.Unauthenticated,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState

    fun initViewModel() {
        viewModelScope.launch {
            val accessToken = withTimeoutOrNull(3000) {
                authRepo.accessTokenFlow.filterNotNull().firstOrNull()
            }
            if (accessToken != null) {
                logDebug("Access token: $accessToken", "AuthViewModel")
                authRepo.refreshCurrentSessionOnLogin()
                _uiState.update { it.copy(userState = UserState.Authenticated(accessToken)) }
            } else {
                logDebug("No access token found", "AuthViewModel")
                _uiState.update { it.copy(isLoading = false, userState = UserState.Unauthenticated) }
            }
        }
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