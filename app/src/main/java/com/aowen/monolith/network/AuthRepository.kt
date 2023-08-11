package com.aowen.monolith.network

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.Discord
import io.github.jan.supabase.gotrue.providers.Github
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.gotrue.user.UserSession
import javax.inject.Inject

interface AuthRepository {
    suspend fun signUpUser(emailFromState: String, passwordFromState: String): Email.Result?
    suspend fun signInWithDiscord()
    suspend fun getCurrentSession(): UserSession?
    suspend fun logout()
}

class AuthRepositoryImpl @Inject constructor(
    private val client: SupabaseClient
) : AuthRepository {

    override suspend fun signUpUser(
        emailFromState: String,
        passwordFromState: String
    ): Email.Result? {
        return client.gotrue.signUpWith(Email) {
            email = emailFromState
            password = passwordFromState
        }
    }

    override suspend fun signInWithDiscord() {
        return client.gotrue.loginWith(Discord)
    }

    override suspend fun getCurrentSession(): UserSession? {
        return client.gotrue.currentSessionOrNull()
    }

    override suspend fun logout() {
        return client.gotrue.logout()
    }
}