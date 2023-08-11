package com.aowen.monolith.viewmodel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.network.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val pageState: PageState = PageState.LOGIN,
    val isLoading: Boolean = false,
    val emailTextField: String = "",
    val passwordTextField: String = "",
    val userId: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun setEmail(text: String) {
        _uiState.update { it.copy(emailTextField = text) }
    }

    fun setPassword(text: String) {
        _uiState.update { it.copy(passwordTextField = text) }
    }

    fun submitLogin() {
        _uiState.update { it.copy(isLoading = true) }
        try {
            viewModelScope.launch {
                authRepo.signInWithDiscord()
            }
        } catch (e: Exception) {
            Log.d("MONOLITH-DEBUG://", e.toString())
        }
        _uiState.update { it.copy(isLoading = false) }
    }
}

enum class PageState {
    LOGIN, SIGNUP
}