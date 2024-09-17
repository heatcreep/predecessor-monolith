package com.aowen.monolith.fakes

import com.aowen.monolith.data.UserInfo
import com.aowen.monolith.fakes.data.fakeUserInfo
import com.aowen.monolith.network.UserRepository
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

    override suspend fun getUser(): UserInfo? {
        return when (userScenario) {
            UserScenario.UserNotFound -> null
            else -> fakeUserInfo
        }
    }

    override suspend fun logout() {
        _logoutCounter.update { it + 1 }
    }
}