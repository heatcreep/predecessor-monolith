package com.aowen.predcompanion.feature.profile.impl

import com.aowen.predcompanion.core.datastore.Theme
import com.aowen.predcompanion.core.model.ui.theme.Console
import com.aowen.predcompanion.core.network.model.NetworkUserState
import com.aowen.predcompanion.core.testing.fakes.data.fakeUserInfo
import com.aowen.predcompanion.core.testing.fakes.preferences.FakeThemePreferences
import com.aowen.predcompanion.core.testing.fakes.preferences.FakeUserPreferencesManager
import com.aowen.predcompanion.core.testing.fakes.repository.AuthScenario
import com.aowen.predcompanion.core.testing.fakes.repository.FakeAuthRepository
import com.aowen.predcompanion.core.testing.fakes.repository.FakeUserRepository
import com.aowen.predcompanion.core.testing.fakes.repository.UserScenario
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

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
            authRepository = FakeAuthRepository(startingUser = NetworkUserState.Authenticated)
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
            authRepository = FakeAuthRepository(startingUser = NetworkUserState.Authenticated)
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
        Assert.assertTrue(userRepository.logoutCounter.value == 0)

        viewModel.handleLogout()
        advanceUntilIdle()

        val expected = 1
        val actual = userRepository.logoutCounter.value

        Assert.assertEquals(expected, actual)
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

        Assert.assertEquals(expected, actual)
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

        Assert.assertTrue(userRepository.logoutCounter.value == 0)

        viewModel.deleteUserAccount()

        advanceUntilIdle()

        val expected = 1
        val actual = userRepository.logoutCounter.value

        Assert.assertEquals(expected, actual)
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

        Assert.assertTrue(authRepository.deleteUserAccountCounter.value == 0)

        viewModel.deleteUserAccount()

        advanceUntilIdle()

        val expected = 1
        val actual = authRepository.deleteUserAccountCounter.value

        Assert.assertEquals(expected, actual)
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

        Assert.assertEquals(expected, actual)
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

        Assert.assertEquals(expected, actual)
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

        Assert.assertEquals(expected, actual)
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

        Assert.assertEquals(expected, actual)

    }
}