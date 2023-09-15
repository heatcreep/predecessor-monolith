package com.aowen.monolith.ui.screens.profile

import android.util.Log
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
        viewModelScope.launch {
            try {
                val userInfoDeferred = async { userRepository.getUser() }

                val userInfo = userInfoDeferred.await()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        userInfo = userInfo
                    )
                }

            } catch (e: Exception) {
                Log.d("MONOLITH_DEBUG: ", e.toString())
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

}