package com.aowen.monolith.fakes

import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAuthRepository(
    private val hasGetPlayerError: Boolean = false,
    private val hasHandleSavePlayerError: Boolean = false,
    private val hasDeleteUserAccountError: Boolean = false,
    override val accessTokenFlow: Flow<String?> = MutableStateFlow(null),
) : AuthRepository {

    private val _deleteUserAccountCounter: MutableStateFlow<Int> = MutableStateFlow(0)
    val deleteUserAccountCounter: MutableStateFlow<Int> = _deleteUserAccountCounter

    companion object {
        const val GetPlayerError = "Failed to get player"
    }

    override suspend fun saveAccessTokenOnSuccessfulLogin() {
        TODO("Not yet implemented")
    }

    override suspend fun signInWithDiscord(): Result<Unit?> {
        return Result.success(Unit)
    }

    override suspend fun getPlayer(): Result<UserProfile?> {
        return if(hasGetPlayerError) {
            Result.failure(Exception(GetPlayerError))
        } else {
            Result.success(UserProfile("validPlayerId"))
        }
    }

    override suspend fun handleSavePlayer(playerId: String): Result<Unit> {
        return if(hasHandleSavePlayerError) {
            Result.failure(Exception("Failed to save player"))
        } else {
            Result.success(Unit)
        }
    }

    override suspend fun deleteUserAccount(userId: String): Result<String> {
        _deleteUserAccountCounter.value++
        return if(hasDeleteUserAccountError) {
            Result.failure(Exception("Failed to delete user account"))
        } else {
            Result.success("User account deleted")
        }
    }

    override suspend fun refreshCurrentSessionOnLogin() {
        TODO("Not yet implemented")
    }
}