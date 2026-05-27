package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.data.repository.user.UserRepository
import com.aowen.predcompanion.core.network.model.NetworkUserInfo
import com.aowen.predcompanion.core.testing.fakes.data.fakeUserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class UserScenario {
    object UserNotFound : UserScenario()
}

class FakeUserRepository(
    private val userScenario: UserScenario? = null
) : UserRepository {

    private val _logoutCounter: MutableStateFlow<Int> = MutableStateFlow(0)
    val logoutCounter: StateFlow<Int> = _logoutCounter

    override suspend fun getUser(): NetworkUserInfo? {
        return when (userScenario) {
            UserScenario.UserNotFound -> null
            else -> fakeUserInfo
        }
    }

    override suspend fun logout() {
        _logoutCounter.update { it + 1 }
    }
}