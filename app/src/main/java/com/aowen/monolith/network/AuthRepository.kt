package com.aowen.monolith.network

import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.providers.Discord
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.gotrue.user.UserSession
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.PostgrestResult
import kotlinx.coroutines.delay
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class UserProfile(
    @SerialName("player_id")
    val playerId: String? = null
)

interface AuthRepository {
    suspend fun signUpUser(emailFromState: String, passwordFromState: String): Email.Result?
    suspend fun signInWithDiscord()
    suspend fun getCurrentSession(): UserSession?
    suspend fun getCurrentUser(): UserInfo?

    suspend fun getPlayer(): Result<UserProfile?>
    suspend fun handleSavePlayer(playerId: String): Result<PostgrestResult>
    suspend fun logout()
}


class AuthRepositoryImpl @Inject constructor(
    private val goTrue: GoTrue,
    private val postgrest: Postgrest
) : AuthRepository {

    override suspend fun signUpUser(
        emailFromState: String,
        passwordFromState: String
    ): Email.Result? {
        return goTrue.signUpWith(Email) {
            email = emailFromState
            password = passwordFromState
        }
    }

    override suspend fun signInWithDiscord() {
        return goTrue.loginWith(Discord)
    }

    override suspend fun getCurrentSession(): UserSession? {
        return goTrue.currentSessionOrNull()
    }

    override suspend fun getPlayer(): Result<UserProfile?> {
        return try {
            var session = goTrue.currentSessionOrNull()
            var retryCount = 3
            while (session == null && retryCount > 0) {
                delay(500)
                session = goTrue.currentSessionOrNull()
                retryCount--
            }
            session?.let {
                if (it.user?.id != null) {
                    Result.success(
                        postgrest.from(TABLE_PROFILES)
                            .select(columns = Columns.raw("player_id")) {
                                eq("id", it.user?.id!!)
                            }.decodeList<UserProfile>().firstOrNull()
                    )
                } else Result.failure(Exception("No user id"))
            } ?: Result.failure(Exception("No current session"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): UserInfo {
        return goTrue.retrieveUserForCurrentSession(true)
    }


    override suspend fun handleSavePlayer(playerId: String): Result<PostgrestResult> {
        return try {
            goTrue.currentSessionOrNull()?.let {
                if (it.user?.id != null) {
                    Result.success(postgrest[TABLE_PROFILES].update({
                        set("player_id", playerId)
                    }) {

                        eq("id", it.user!!.id)
                    })
                } else Result.failure(Exception("No user id"))
            } ?: Result.failure(Exception("No current session"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun logout() {
        return goTrue.logout()
    }
}