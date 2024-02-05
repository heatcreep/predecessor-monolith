package com.aowen.monolith.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.UserInfo
import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val userInfo: UserInfo? = null,
)

enum class ProfileToastState {
    DELETE,
    LOGOUT,
    ERROR,
    NONE
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState: StateFlow<ProfileScreenUiState> = _uiState

    private val _showProfileToast = MutableStateFlow(ProfileToastState.NONE)
    val showProfileToast = _showProfileToast

    init {
        initViewModel()
    }

    fun handleLogout() {
        viewModelScope.launch {
            userRepository.logout()
            _showProfileToast.emit(ProfileToastState.LOGOUT)
        }
    }

    fun initViewModel() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val userInfoDeferred = async { userRepository.getUser() }

            val userInfo = userInfoDeferred.await()

            if (userInfo != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        userInfo = userInfo
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error getting user info.",
                    )
                }
            }
        }
    }

    fun deleteUserAccount() {
        viewModelScope.launch {
            val userId = userRepository.getUser()?.id
            if (userId != null) {
                val deleteResult = authRepository.deleteUserAccount(userId.toString())
                if(deleteResult.isSuccess) {
                    userRepository.logout()
                    _showProfileToast.emit(ProfileToastState.DELETE)
                } else {
                    _showProfileToast.emit(ProfileToastState.ERROR)
                }
            } else {
                _showProfileToast.emit(ProfileToastState.ERROR)
            }
        }
    }

    fun onShowToastComplete() {
        _showProfileToast.value = ProfileToastState.NONE
    }
}