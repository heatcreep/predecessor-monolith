package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.data.repository.user.UserRepository
import com.aowen.predcompanion.core.model.data.CurrentUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class UserScenario {
    object UserNotFound : UserScenario()
}

class FakeUserRepository(
    private val userScenario: UserScenario? = null
) : UserRepository {

    private val _currentUser = MutableStateFlow(
        when (userScenario) {
            UserScenario.UserNotFound -> null
            else -> CurrentUser(
                id = "fake-user-id",
                name = "Fake User",
                players = emptyList(),
            )
        }
    )

    override val currentUser: Flow<CurrentUser?> = _currentUser

    private val _clearUserCounter: MutableStateFlow<Int> = MutableStateFlow(0)
    val clearUserCounter: StateFlow<Int> = _clearUserCounter

    override suspend fun fetchCurrentUser(): CurrentUser? = _currentUser.value

    override suspend fun sync() {
        // no-op in tests
    }

    override suspend fun clearUser() {
        _currentUser.value = null
        _clearUserCounter.update { it + 1 }
    }
}
