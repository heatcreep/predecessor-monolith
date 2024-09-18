@file:OptIn(ExperimentalCoroutinesApi::class)

package com.aowen.monolith.ui

import com.aowen.monolith.data.Console
import com.aowen.monolith.data.datastore.Theme
import com.aowen.monolith.fakes.AuthScenario
import com.aowen.monolith.fakes.FakeAuthRepository
import com.aowen.monolith.fakes.FakeThemePreferences
import com.aowen.monolith.fakes.FakeUserPreferencesManager
import com.aowen.monolith.fakes.FakeUserRepository
import com.aowen.monolith.fakes.UserScenario
import com.aowen.monolith.fakes.data.fakeUserInfo
import com.aowen.monolith.feature.profile.ProfileScreenState
import com.aowen.monolith.feature.profile.ProfileToastState
import com.aowen.monolith.feature.profile.ProfileViewModel
import com.aowen.monolith.network.UserState
import com.aowen.monolith.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
            themePreferences = FakeThemePreferences(),
            userRepository = FakeUserRepository(),
            authRepository = FakeAuthRepository()
        )
    }

    @Test
    fun `Unauthenticated - when initViewModel is called, then uiState is set properly`() = runTest {

        val expected = ProfileScreenState.UserInfoLoaded(Console.PC, Theme.LIGHT, null)
        val actual = viewModel.uiState.value

        assertEquals(expected, actual)
    }

    @Test
    fun `Authenticated = when initViewModel is called, and user is null, then uiState is set properly`() = runTest {
        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            themePreferences = FakeThemePreferences(),
            userRepository = FakeUserRepository(userScenario = UserScenario.UserNotFound),
            authRepository = FakeAuthRepository(startingUser = UserState.Authenticated)
        )

        advanceUntilIdle()

        val expected = ProfileScreenState.Error(Console.PC, Theme.LIGHT, "Error loading user info")
        val actual = viewModel.uiState.value

        assertEquals(expected, actual)
    }

    @Test
    fun `Authenticated = when initViewModel is called, and user is found, then uiState is set properly`() = runTest {
        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            themePreferences = FakeThemePreferences(),
            userRepository = FakeUserRepository(),
            authRepository = FakeAuthRepository(startingUser = UserState.Authenticated)
        )

        advanceUntilIdle()

        val expected = ProfileScreenState.UserInfoLoaded(Console.PC, Theme.LIGHT, fakeUserInfo)
        val actual = viewModel.uiState.value

        assertEquals(expected, actual)
    }

    @Test
    fun `handleLogout() calls userRepository logout`() = runTest {
        val userRepository = FakeUserRepository()
        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            themePreferences = FakeThemePreferences(),
            userRepository = userRepository,
            authRepository = FakeAuthRepository()
        )
        advanceUntilIdle()
        assertTrue(userRepository.logoutCounter.value == 0)

        viewModel.handleLogout()
        advanceUntilIdle()

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
        advanceUntilIdle()
        job.cancel()

        assertEquals(expected, actual)
    }

    @Test
    fun `deleteUserAccount() calls userRepository logout`() = runTest {
        val userRepository = FakeUserRepository()
        val authRepository = FakeAuthRepository()
        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            themePreferences = FakeThemePreferences(),
            userRepository = userRepository,
            authRepository = authRepository
        )

        advanceUntilIdle()

        assertTrue(userRepository.logoutCounter.value == 0)

        viewModel.deleteUserAccount()

        advanceUntilIdle()

        val expected = 1
        val actual = userRepository.logoutCounter.value

        assertEquals(expected, actual)
    }

    @Test
    fun `deleteUserAccount() calls authRepository deleteUserAccount`() = runTest {
        val userRepository = FakeUserRepository()
        val authRepository = FakeAuthRepository()
        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            themePreferences = FakeThemePreferences(),
            userRepository = userRepository,
            authRepository = authRepository
        )

        assertTrue(authRepository.deleteUserAccountCounter.value == 0)

        viewModel.deleteUserAccount()

        advanceUntilIdle()

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

        advanceUntilIdle()
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

        val userRepository = FakeUserRepository()
        val authRepository = FakeAuthRepository(
            errorScenario = AuthScenario.DeleteUserAccountError
        )
        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            themePreferences = FakeThemePreferences(),
            userRepository = userRepository,
            authRepository = authRepository
        )

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.showProfileToast.collect { state -> actual.add(state) }
        }

        viewModel.deleteUserAccount()

        advanceUntilIdle()
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
        val authRepository = FakeAuthRepository(errorScenario = AuthScenario.DeleteUserAccountError)
        viewModel = ProfileViewModel(
            userPreferencesDataStore = FakeUserPreferencesManager(),
            themePreferences = FakeThemePreferences(),
            userRepository = userRepository,
            authRepository = authRepository
        )

        advanceUntilIdle()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.showProfileToast.collect { state -> actual.add(state) }
        }

        viewModel.deleteUserAccount()

        advanceUntilIdle()
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