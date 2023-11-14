package com.aowen.monolith.fakes

import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.network.UserProfile

class FakeAuthRepository(
    private val hasGetPlayerError: Boolean = false,
    private val hasHandleSavePlayerError: Boolean = false
) : AuthRepository {

    companion object {
        const val GetPlayerError = "Failed to get player"
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
}