package com.aowen.monolith.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.logDebug
import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState

    val userState = authRepo.userState

    fun initViewModel() {
        viewModelScope.launch {
            try {
                authRepo.getCurrentSessionStatus()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "There was an issue signing you in. Please try again."
                    )
                }
            }

        }
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
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

    fun handleSkipOnboarding() {
        viewModelScope.launch {
            userPreferencesManager.setSkippedOnboarding()
        }
    }
}