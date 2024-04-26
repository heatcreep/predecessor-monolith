package com.aowen.monolith.ui

import com.aowen.monolith.data.Console
import com.aowen.monolith.fakes.FakeAuthRepository
import com.aowen.monolith.fakes.FakeUserPreferencesManager
import com.aowen.monolith.fakes.FakeUserRepository
import com.aowen.monolith.fakes.data.fakeUserInfo
import com.aowen.monolith.feature.profile.ProfileScreenUiState
import com.aowen.monolith.feature.profile.ProfileToastState
import com.aowen.monolith.feature.profile.ProfileViewModel
import com.aowen.monolith.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            userRepository = FakeUserRepository(),
            authRepository = FakeAuthRepository()
        )
    }

    @Test
    fun `when initViewModel is called, then uiState is updated with userInfo`() {

        val expected = ProfileScreenUiState(
            isLoading = false,
            error = null,
            userInfo = fakeUserInfo,
            console = Console.PC
        )
        val actual = viewModel.uiState.value

        assertEquals(expected, actual)
    }

    @Test
    fun `when initViewModel is called, then uiState is error when getUser returns null`() {

        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            userRepository = FakeUserRepository(error = true),
            authRepository = FakeAuthRepository()
        )

        val expected = ProfileScreenUiState(
            isLoading = false,
            error = "Error getting user info.",
            console = Console.PC
        )
        val actual = viewModel.uiState.value

        assertEquals(expected, actual)
    }

    @Test
    fun `handleLogout() calls userRepository logout`() {
        val userRepository = FakeUserRepository()
        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            userRepository = userRepository,
            authRepository = FakeAuthRepository()
        )

        assertTrue(userRepository.logoutCounter.value == 0)

        viewModel.handleLogout()

        val expected = 1
        val actual = userRepository.logoutCounter.value

        assertEquals(expected, actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `handleLogout() emits the correct events`() = runTest {
        val expected = listOf(
            ProfileToastState.NONE,
            ProfileToastState.LOGOUT,
        )
        val actual = mutableListOf<ProfileToastState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.showProfileToast.collect { state -> actual.add(state) }
        }

        viewModel.handleLogout()
        job.cancel()

        assertEquals(expected, actual)
    }

    @Test
    fun `deleteUserAccount() calls userRepository logout`() {
        val userRepository = FakeUserRepository()
        val authRepository = FakeAuthRepository()
        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            userRepository = userRepository,
            authRepository = authRepository
        )

        assertTrue(userRepository.logoutCounter.value == 0)

        viewModel.deleteUserAccount()

        val expected = 1
        val actual = userRepository.logoutCounter.value

        assertEquals(expected, actual)
    }

    @Test
    fun `deleteUserAccount() calls authRepository deleteUserAccount`() {
        val userRepository = FakeUserRepository()
        val authRepository = FakeAuthRepository()
        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            userRepository = userRepository,
            authRepository = authRepository
        )

        assertTrue(authRepository.deleteUserAccountCounter.value == 0)

        viewModel.deleteUserAccount()

        val expected = 1
        val actual = authRepository.deleteUserAccountCounter.value

        assertEquals(expected, actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `deleteUserAccount() on success emits the right snackbar flow`() = runTest {
        val expected = listOf(
            ProfileToastState.NONE,
            ProfileToastState.DELETE,
        )
        val actual = mutableListOf<ProfileToastState>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.showProfileToast.collect { state -> actual.add(state) }
        }

        viewModel.deleteUserAccount()
        job.cancel()

        assertEquals(expected, actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `deleteUserAccount() on failure to find user emits the right snackbar flow`() = runTest {
        val expected = listOf(
            ProfileToastState.NONE,
            ProfileToastState.ERROR,
        )
        val actual = mutableListOf<ProfileToastState>()

        val userRepository = FakeUserRepository(error = true)
        val authRepository = FakeAuthRepository()
        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            userRepository = userRepository,
            authRepository = authRepository
        )

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.showProfileToast.collect { state -> actual.add(state) }
        }

        viewModel.deleteUserAccount()
        job.cancel()

        assertEquals(expected, actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `deleteUserAccount() on failure to delete user emits the right snackbar flow`() = runTest {
        val expected = listOf(
            ProfileToastState.NONE,
            ProfileToastState.ERROR,
        )
        val actual = mutableListOf<ProfileToastState>()

        val userRepository = FakeUserRepository()
        val authRepository = FakeAuthRepository(hasDeleteUserAccountError = true)
        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            userRepository = userRepository,
            authRepository = authRepository
        )

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.showProfileToast.collect { state -> actual.add(state) }
        }

        viewModel.deleteUserAccount()
        job.cancel()

        assertEquals(expected, actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onShowToastComplete() sets showDeleteAccountToast to NONE`() = runTest {

        val expected = listOf(
            ProfileToastState.NONE,
            ProfileToastState.DELETE,
            ProfileToastState.NONE
        )
        val actual = mutableListOf<ProfileToastState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.showProfileToast.collect { state -> actual.add(state) }
        }

        viewModel.showProfileToast.emit(ProfileToastState.DELETE)
        viewModel.onShowToastComplete()
        job.cancel()

        assertEquals(expected, actual)

    }
}