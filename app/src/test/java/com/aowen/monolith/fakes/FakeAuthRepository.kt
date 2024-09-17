package com.aowen.monolith.fakes

import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.UserProfile
import com.aowen.monolith.network.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class AuthScenario {
    object LoginError : AuthScenario()
    object Unauthenticated : AuthScenario()
    object UnauthenticatedSkipOnboarding : AuthScenario()
    object SessionStatusError : AuthScenario()
    object NoPlayerFound : AuthScenario()
    object NoCurrentSession : AuthScenario()
    object NetworkSessionError : AuthScenario()
    object FailedCurrentSession : AuthScenario()
    object NoUserId : AuthScenario()
    object SavePlayerError : AuthScenario()
    object NoAccessTokenError : AuthScenario()
    object DeleteUserAccountError : AuthScenario()
}

abstract class AuthTokenScenario {
    object ValidAccessToken : AuthTokenScenario()
    object NoAccessToken : AuthTokenScenario()
}

class FakeAuthRepository(
    private val startingUser: UserState = UserState.Unauthenticated(false),
    private val errorScenario: AuthScenario? = null,
) : AuthRepository {

    private val _fakeUserState: MutableStateFlow<UserState> = MutableStateFlow(startingUser)
    override val userState: StateFlow<UserState> = _fakeUserState

    private val _deleteUserAccountCounter: MutableStateFlow<Int> = MutableStateFlow(0)
    val deleteUserAccountCounter: MutableStateFlow<Int> = _deleteUserAccountCounter

    private val _getCurrentSessionStatusCounter: MutableStateFlow<Int> = MutableStateFlow(0)
    val getCurrentSessionStatusCounter: MutableStateFlow<Int> = _getCurrentSessionStatusCounter

    private val _saveAccessTokenCounter: MutableStateFlow<Int> = MutableStateFlow(0)
    val saveAccessTokenCounter: MutableStateFlow<Int> = _saveAccessTokenCounter

    companion object {
        const val GetPlayerError = "Failed to get player"
    }

    override suspend fun handleSuccessfulLoginFromDiscord() {
        _saveAccessTokenCounter.value++
        if (errorScenario == AuthScenario.NoAccessTokenError) {
            // Do nothing
        } else {
            _fakeUserState.update {
                UserState.Authenticated
            }
        }
    }

    override suspend fun signInWithDiscord(): Result<Unit?> {
        return if (errorScenario == AuthScenario.LoginError) {
            Result.failure(Exception("Failed to login"))
        } else {
            Result.success(Unit)
        }
    }

    override suspend fun getPlayer(): Result<UserProfile?> {
        return when (errorScenario) {
            AuthScenario.NoCurrentSession -> Result.failure(Exception("No current session"))
            AuthScenario.NoUserId -> Result.failure(Exception("No user id"))
            AuthScenario.NoPlayerFound -> Result.failure(Exception(GetPlayerError))
            else -> Result.success(UserProfile("validPlayerId"))
        }
    }

    override suspend fun handleSavePlayer(playerId: String): Result<Unit> {
        return when (errorScenario) {
            AuthScenario.NoCurrentSession -> Result.failure(Exception("No current session"))
            AuthScenario.NoUserId -> Result.failure(Exception("No user id"))
            AuthScenario.SavePlayerError -> Result.failure(Exception("Failed to save player"))
            else -> Result.success(Unit)
        }
    }

    override suspend fun deleteUserAccount(userId: String): Result<String> {
        _deleteUserAccountCounter.value++
        return when (errorScenario) {
            AuthScenario.DeleteUserAccountError -> Result.failure(Exception("Failed to delete user account"))
            else -> Result.success("User account deleted")
        }
    }

    override suspend fun getCurrentSessionStatus() {
        _getCurrentSessionStatusCounter.value++
        when (errorScenario) {
            AuthScenario.Unauthenticated -> _fakeUserState.update {
                UserState.Unauthenticated(false)
            }
            AuthScenario.UnauthenticatedSkipOnboarding -> _fakeUserState.update {
                UserState.Unauthenticated(true)
            }
            AuthScenario.SessionStatusError -> throw Exception("Something went wrong fetching session status.")
            AuthScenario.NetworkSessionError -> throw Exception("Failed to get current session, Network Error")
            else -> {
                _fakeUserState.update {
                    UserState.Authenticated
                }
            }
        }
    }
}