package com.aowen.predcompanion.core.data.repository.user

import com.aowen.predcompanion.core.data.model.asCurrentUser
import com.aowen.predcompanion.core.data.model.asEntity
import com.aowen.predcompanion.core.data.repository.user.UserState.SignedIn
import com.aowen.predcompanion.core.database.dao.CurrentUserDao
import com.aowen.predcompanion.core.datastore.AuthStateDataStore
import com.aowen.predcompanion.core.model.data.CurrentUser
import com.aowen.predcompanion.core.network.PredGGNetworkDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

sealed interface UserState {
    data object Loading : UserState
    data class SignedIn(val currentUser: CurrentUser) : UserState
    data object SignedOut : UserState
    data class Error(val message: String) : UserState
}

interface UserRepository {

    val currentUserState: Flow<UserState>

    suspend fun fetchCurrentUser(): CurrentUser?

    suspend fun sync()

    suspend fun clearUser()
}

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class OfflineFirstUserRepository @Inject constructor(
    private val predGGNetwork: PredGGNetworkDataSource,
    private val currentUserDao: CurrentUserDao,
    private val authStore: AuthStateDataStore,
) : UserRepository {

    private val _currentUserState = MutableStateFlow<UserState>(UserState.Loading)
    override val currentUserState: Flow<UserState> = _currentUserState

    init {
        combine(
            authStore.authStateFlow.map { it?.isAuthorized == true },
            currentUserDao.getCurrentUser()
        ) { isLoggedIn, currentUser ->
            if (isLoggedIn) {
                if (currentUser != null) {
                    currentUser.asCurrentUser().let(::SignedIn)
                } else {
                    sync()
                    UserState.Loading
                }
            }
            else UserState.SignedOut
        }.onEach { state ->
            _currentUserState.emit(state)
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    override suspend fun fetchCurrentUser(): CurrentUser? {
        
        val data = predGGNetwork.getCurrentUser().data
        return data?.currentUser?.userFragment?.asCurrentUser()
    }

    override suspend fun sync() {
        _currentUserState.emit(UserState.Loading)
        try {
            val data = predGGNetwork.getCurrentUser().data
            val entity = data?.currentUser?.userFragment?.asEntity()
            if (entity != null) {
                currentUserDao.upsertCurrentUser(entity)
            }
        } catch (_: Exception) {
            // Keep cached user on network failure
            _currentUserState.emit(UserState.SignedOut)
        }
    }

    override suspend fun clearUser() {
        _currentUserState.emit(UserState.Loading)
        authStore.clear()
        currentUserDao.deleteCurrentUser()
        _currentUserState.emit(UserState.SignedOut)
    }
}
