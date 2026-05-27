package com.aowen.predcompanion.feature.profile.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.predcompanion.core.data.repository.auth.AuthRepository
import com.aowen.predcompanion.core.data.repository.user.UserRepository
import com.aowen.predcompanion.core.datastore.Theme
import com.aowen.predcompanion.core.datastore.ThemePreferences
import com.aowen.predcompanion.core.datastore.UserPreferencesManager
import com.aowen.predcompanion.core.model.ui.theme.Console
import com.aowen.predcompanion.core.network.model.NetworkUserInfo
import com.aowen.predcompanion.core.network.model.NetworkUserState
import com.aowen.predcompanion.logDebug
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class ProfileScreenState {

    data object Loading : ProfileScreenState()
    data class Error(
        val console: Console,
        val theme: Theme,
        val message: String
    ) : ProfileScreenState()

    data class UserInfoLoaded(
        val console: Console,
        val theme: Theme,
        val userInfo: NetworkUserInfo?
    ) : ProfileScreenState()
}

data class ProfileScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val userInfo: NetworkUserInfo? = null,
    val console: Console = Console.PC
)

enum class ProfileToastState {
    DELETE,
    LOGOUT,
    ERROR,
    NONE
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesManager,
    private val themePreferences: ThemePreferences,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProfileScreenState> =
        MutableStateFlow(ProfileScreenState.Loading)
    val uiState: StateFlow<ProfileScreenState> = _uiState

    private val _showProfileToast = MutableStateFlow(ProfileToastState.NONE)
    val showProfileToast = _showProfileToast

    init {
        observeUserState()
    }

    fun handleLogout() {
        viewModelScope.launch {
            userRepository.logout()
            _showProfileToast.emit(ProfileToastState.LOGOUT)
        }
    }

    private fun observeUserState() {
        authRepository.networkUserState
            .onEach { userState ->
                val console = userPreferencesDataStore.console.first()
                val theme = themePreferences.theme.first()
                when (userState) {
                    is NetworkUserState.Loading -> {
                        _uiState.update { ProfileScreenState.Loading }
                    }

                    is NetworkUserState.Unauthenticated -> {
                        _uiState.update {
                            ProfileScreenState.UserInfoLoaded(
                                console = console,
                                theme = theme,
                                userInfo = null
                            )
                        }
                    }

                    is NetworkUserState.Authenticated -> {
                        val user = userRepository.getUser()
                        _uiState.update {
                            if (user != null) {
                                ProfileScreenState.UserInfoLoaded(
                                    console = console,
                                    theme = theme,
                                    userInfo = user
                                )
                            } else {
                                ProfileScreenState.Error(
                                    console = console,
                                    theme = theme,
                                    message = "Error loading user info"
                                )
                            }
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun saveConsole(console: Console) {
        viewModelScope.launch {
            userPreferencesDataStore.saveConsole(console)
            _uiState.update { currentState ->
                when (currentState) {
                    is ProfileScreenState.UserInfoLoaded -> {
                        currentState.copy(console = console)
                    }

                    is ProfileScreenState.Error -> {
                        currentState.copy(console = console)
                    }

                    else -> {
                        currentState
                    }
                }
            }
        }
    }

    fun saveTheme(theme: Theme) {
        viewModelScope.launch {
            themePreferences.saveTheme(theme)
            _uiState.update { currentState ->
                when (currentState) {
                    is ProfileScreenState.UserInfoLoaded -> {
                        currentState.copy(theme = theme)
                    }

                    is ProfileScreenState.Error -> {
                        currentState.copy(theme = theme)
                    }

                    else -> {
                        currentState
                    }
                }
            }
        }
    }

    fun submitLogin() {
        _uiState.update { ProfileScreenState.Loading }
        try {
            viewModelScope.launch {
                authRepository.signInWithDiscord()
            }
        } catch (e: Exception) {
            logDebug(e.toString())
            _uiState.update { currentState ->
                when (currentState) {
                    is ProfileScreenState.Error -> {
                        ProfileScreenState.Error(
                            console = currentState.console,
                            theme = currentState.theme,
                            message = "There was an issue signing you in. Please try again."
                        )
                    }

                    else -> {
                        currentState
                    }

                }
            }
        }
    }

    fun deleteUserAccount() {
        viewModelScope.launch {
            val userId = userRepository.getUser()?.id
            if (userId != null) {
                val deleteResult = authRepository.deleteUserAccount(userId.toString())
                if (deleteResult.isSuccess) {
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