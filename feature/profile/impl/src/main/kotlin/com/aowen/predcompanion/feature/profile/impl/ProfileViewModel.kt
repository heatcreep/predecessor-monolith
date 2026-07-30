package com.aowen.predcompanion.feature.profile.impl

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.aowen.predcompanion.core.data.model.MatchHistoryTarget
import com.aowen.predcompanion.core.data.repository.auth.NewAuthRepository
import com.aowen.predcompanion.core.data.repository.matches.MatchHistoryRepository
import com.aowen.predcompanion.core.data.repository.players.PlayerRepository
import com.aowen.predcompanion.core.data.repository.user.UserRepository
import com.aowen.predcompanion.core.data.repository.user.UserState
import com.aowen.predcompanion.core.datastore.Theme
import com.aowen.predcompanion.core.datastore.ThemePreferences
import com.aowen.predcompanion.core.datastore.UserPreferencesManager
import com.aowen.predcompanion.core.model.ui.theme.Console
import com.aowen.predcompanion.core.ui.model.HeroStatisticsUiModel
import com.aowen.predcompanion.core.ui.model.MatchListItemUiModel
import com.aowen.predcompanion.core.ui.model.PlayerProfileCardUiModel
import com.aowen.predcompanion.core.ui.model.mapper.MatchListItemUiMapper
import com.aowen.predcompanion.core.ui.model.toHeroStatisticsUiModel
import com.aowen.predcompanion.core.ui.model.toPlayerProfileCardUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HeroStatsUiState {
    data object Loading : HeroStatsUiState
    data class Loaded(val stats: List<HeroStatisticsUiModel>) : HeroStatsUiState
    data class Error(val message: String) : HeroStatsUiState
}

data class CurrentUserUiModel(
    val name: String,
    val players: List<PlayerProfileCardUiModel>
)

data class ProfileScreenState(
    val console: Console = Console.PC,
    val theme: Theme = Theme.SYSTEM,
    val user: UserUiState = UserUiState.Loading,
)

sealed interface UserUiState {

    object Loading : UserUiState
    object SignedOut : UserUiState
    data class Error(
        val message: String
    ) : UserUiState

    data class UserInfoLoaded(
        val userInfo: CurrentUserUiModel? = null
    ) : UserUiState
}

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
    private val newAuthRepository: NewAuthRepository,
    private val playerRepository: PlayerRepository,
    private val matchHistoryRepository: MatchHistoryRepository,
    private val matchListItemUiMapper: MatchListItemUiMapper,

    ) : ViewModel() {

    val uiState: StateFlow<ProfileScreenState> =
        combine(
            userRepository.currentUserState,
            themePreferences.theme,
            userPreferencesDataStore.console,
        ) { currentUserState, theme, console ->
            val user = when (currentUserState) {
                is UserState.Loading -> UserUiState.Loading
                is UserState.SignedOut -> UserUiState.SignedOut
                is UserState.Error -> UserUiState.Error(message = currentUserState.message)
                is UserState.SignedIn -> UserUiState.UserInfoLoaded(
                    userInfo = CurrentUserUiModel(
                        name = currentUserState.currentUser.name,
                        players = currentUserState.currentUser.players.map { player -> player.toPlayerProfileCardUiModel() }
                    )
                )
            }
            ProfileScreenState(console = console, theme = theme, user = user)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            ProfileScreenState()
        )

    // Re-derives from auth state directly (not `uiState`) so it isn't coupled to
    // theme/console emissions and cancels/restarts cleanly on sign-in/sign-out.
    val matchHistory: Flow<PagingData<MatchListItemUiModel>> =
        userRepository.currentUserState
            .flatMapLatest { userState ->
                when (userState) {
                    is UserState.SignedIn -> matchHistoryRepository.getMatchHistoryFor(
                        MatchHistoryTarget.CurrentUser
                    ).map { pagingData ->
                        pagingData.map {
                            matchListItemUiMapper.buildFrom(it)
                        }
                    }
                    else -> emptyFlow()
                }
            }
            .cachedIn(viewModelScope)

    val heroStats: StateFlow<HeroStatsUiState> =
        userRepository.currentUserState
            .mapLatest { userState ->
                when (userState) {
                    is UserState.SignedIn -> {
                        userRepository.fetchCurrentUser()?.let {
                            HeroStatsUiState.Loaded(it.players.first().heroStatistics.map { it.toHeroStatisticsUiModel() })
                        } ?: HeroStatsUiState.Error("No current user")
                    }
                    else -> HeroStatsUiState.Loading
                }
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                HeroStatsUiState.Loading
            )

    private val _showProfileToast = MutableStateFlow(ProfileToastState.NONE)
    val showProfileToast = _showProfileToast

    fun handleLogout() {
        viewModelScope.launch {
            userRepository.clearUser()
            _showProfileToast.emit(ProfileToastState.LOGOUT)
        }
    }

    fun saveConsole(console: Console) {
        viewModelScope.launch {
            userPreferencesDataStore.saveConsole(console)
        }
    }

    fun saveTheme(theme: Theme) {
        viewModelScope.launch {
            themePreferences.saveTheme(theme)
        }
    }

    fun loginIntent() = newAuthRepository.loginIntent()

    fun onLoginResult(data: Intent?) {
        if (data == null) return
        viewModelScope.launch {
            newAuthRepository.onLoginResult(data)
        }
    }

    fun onShowToastComplete() {
        _showProfileToast.value = ProfileToastState.NONE
    }
}