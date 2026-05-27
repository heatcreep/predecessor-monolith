package com.aowen.predcompanion.core.data.repository.user

import com.aowen.predcompanion.core.network.SupabaseAuthService
import com.aowen.predcompanion.core.network.SupabasePostgrestService
import com.aowen.predcompanion.core.network.model.NetworkUserInfo
import kotlinx.coroutines.delay
import javax.inject.Inject

interface UserRepository {

    suspend fun getUser(): NetworkUserInfo?

    suspend fun logout()
}

class NetworkUserRepository @Inject constructor(
    private val authService: SupabaseAuthService,
    private val postgrestService: SupabasePostgrestService,
) : UserRepository {

    override suspend fun getUser(): NetworkUserInfo? {
        var session = authService.currentSession()
        var retryCount = 3
        while (session == null && retryCount > 0) {
            delay(500)
            session = authService.currentSession()
            retryCount--
        }
        val user = session?.let {
            if (it.user?.id != null) {
                postgrestService.fetchUserInfo(it.user?.email!!)
            } else null
        }

        return user
    }

    override suspend fun logout() {
        authService.signOut()
    }
}
