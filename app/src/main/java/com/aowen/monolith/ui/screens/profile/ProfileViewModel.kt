package com.aowen.monolith.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.UserInfo
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

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState: StateFlow<ProfileScreenUiState> = _uiState

    fun handleLogout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    init {
        initViewModel()
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

}