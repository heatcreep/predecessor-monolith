package com.aowen.predcompanion.core.testing.fakes.repository

import com.aowen.predcompanion.core.data.repository.user.UserRepository
import com.aowen.predcompanion.core.data.repository.user.UserState
import com.aowen.predcompanion.core.model.data.CurrentUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserRepository : UserRepository {

    private val _currentUserState = MutableStateFlow<UserState>(UserState.Loading)
    override val currentUserState: Flow<UserState> = _currentUserState

    var currentUserToReturn: CurrentUser? = null

    override suspend fun fetchCurrentUser(): CurrentUser? = currentUserToReturn

    override suspend fun sync() {}

    override suspend fun clearUser() {
        _currentUserState.value = UserState.SignedOut
    }

    fun emitUserState(state: UserState) {
        _currentUserState.value = state
    }
}
