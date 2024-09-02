package com.aowen.monolith.fakes

import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.UserProfile
import com.aowen.monolith.network.UserState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class AuthErrorScenario {
    object LoginError : AuthErrorScenario()
    object NoUserFound : AuthErrorScenario()
    object NoPlayerFound : AuthErrorScenario()
    object NoCurrentSession : AuthErrorScenario()
    object FailedCurrentSession : AuthErrorScenario()
    object NoUserId : AuthErrorScenario()
    object SavePlayerError : AuthErrorScenario()
    object NoAccessTokenError : AuthErrorScenario()
    object DeleteUserAccountError : AuthErrorScenario()
}

abstract class AuthTokenScenario {
    object ValidAccessToken : AuthTokenScenario()
    object NoAccessToken : AuthTokenScenario()
}

class FakeAuthRepository(
    private val errorScenario: AuthErrorScenario? = null,
    private val tokenScenario: AuthTokenScenario? = null,
) : AuthRepository {

    private val _fakeUserState = MutableStateFlow<UserState>(UserState.Unauthenticated)
    override val userState: StateFlow<UserState> = _fakeUserState

    override val accessTokenFlow: Flow<String?>
        get() {
            return when (tokenScenario) {
                AuthTokenScenario.ValidAccessToken -> MutableStateFlow("validAccessToken")
                else -> MutableStateFlow(null)
            }
        }

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
        if (errorScenario == AuthErrorScenario.NoAccessTokenError) {
            // Do nothing
        } else {
            _fakeUserState.update {
                UserState.Authenticated
            }
        }
    }

    override suspend fun signInWithDiscord(): Result<Unit?> {
        return if (errorScenario == AuthErrorScenario.LoginError) {
            Result.failure(Exception("Failed to login"))
        } else {
            Result.success(Unit)
        }
    }

    override suspend fun getPlayer(): Result<UserProfile?> {
        return when (errorScenario) {
            AuthErrorScenario.NoCurrentSession -> Result.failure(Exception("No current session"))
            AuthErrorScenario.NoUserId -> Result.failure(Exception("No user id"))
            AuthErrorScenario.NoPlayerFound -> Result.failure(Exception(GetPlayerError))
            else -> Result.success(UserProfile("validPlayerId"))
        }
    }

    override suspend fun handleSavePlayer(playerId: String): Result<Unit> {
        return when (errorScenario) {
            AuthErrorScenario.NoCurrentSession -> Result.failure(Exception("No current session"))
            AuthErrorScenario.NoUserId -> Result.failure(Exception("No user id"))
            AuthErrorScenario.SavePlayerError -> Result.failure(Exception("Failed to save player"))
            else -> Result.success(Unit)
        }
    }

    override suspend fun deleteUserAccount(userId: String): Result<String> {
        _deleteUserAccountCounter.value++
        return when (errorScenario) {
            AuthErrorScenario.DeleteUserAccountError -> Result.failure(Exception("Failed to delete user account"))
            else -> Result.success("User account deleted")
        }
    }

    override suspend fun getCurrentSessionStatus() {
        _getCurrentSessionStatusCounter.value++
        when (errorScenario) {
            AuthErrorScenario.NoCurrentSession -> _fakeUserState.update {
                UserState.Unauthenticated
            }
            AuthErrorScenario.FailedCurrentSession -> throw Exception("Failed to get current session")
            else -> {
                _fakeUserState.update {
                    UserState.Authenticated
                }
            }
        }
    }
}