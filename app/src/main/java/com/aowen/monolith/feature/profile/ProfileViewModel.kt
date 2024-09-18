package com.aowen.monolith.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aowen.monolith.data.Console
import com.aowen.monolith.data.UserInfo
import com.aowen.monolith.data.datastore.Theme
import com.aowen.monolith.data.datastore.ThemePreferences
import com.aowen.monolith.logDebug
import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.UserPreferencesManager
import com.aowen.monolith.network.UserRepository
import com.aowen.monolith.network.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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
        val userInfo: UserInfo?
    ) : ProfileScreenState()
}

data class ProfileScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val userInfo: UserInfo? = null,
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
        initViewModel()
    }

    fun handleLogout() {
        viewModelScope.launch {
            userRepository.logout()
            _showProfileToast.emit(ProfileToastState.LOGOUT)
        }
    }

    fun initViewModel() {
        viewModelScope.launch {
            val console = userPreferencesDataStore.console.first()
            val theme = themePreferences.theme.first()
            when (authRepository.userState.value) {
                is UserState.Unauthenticated -> {
                    _uiState.update {
                        ProfileScreenState.UserInfoLoaded(
                            console = console,
                            theme = theme,
                            userInfo = null
                        )
                    }
                }

                is UserState.Authenticated -> {
                    val user = userRepository.getUser()
                    if (user != null) {
                        _uiState.update {
                            ProfileScreenState.UserInfoLoaded(
                                console = console,
                                theme = theme,
                                userInfo = user
                            )
                        }
                    } else {
                        _uiState.update {
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