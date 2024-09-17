package com.aowen.monolith.feature.auth

import com.aowen.monolith.fakes.AuthScenario
import com.aowen.monolith.fakes.FakeAuthRepository
import com.aowen.monolith.fakes.FakeUserPreferencesManager
import com.aowen.monolith.network.UserState
import com.aowen.monolith.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LoginViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initViewModel should update uiState with loading false and error set when awaitAuthService fails`() =
        runTest {
            viewModel = LoginViewModel(
                authRepo = FakeAuthRepository(
                    errorScenario = AuthScenario.SessionStatusError
                ),
                userPreferencesManager = FakeUserPreferencesManager()
            )
            viewModel.initViewModel()
            advanceUntilIdle()
            val expected = LoginUiState(
                isLoading = false,
                errorMessage = "There was an issue signing you in. Please try again."
            )
            assertEquals(
                expected,
                viewModel.uiState.value
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initViewModel should update user state when session is found`() = runTest {
        viewModel = LoginViewModel(
            authRepo = FakeAuthRepository(),
            userPreferencesManager = FakeUserPreferencesManager()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        assertEquals(UserState.Authenticated,
            viewModel.userState.value
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initViewModel should update user state when no session is found and user has not skipped onboarding`() = runTest {
        viewModel = LoginViewModel(
            authRepo = FakeAuthRepository(
                errorScenario = AuthScenario.Unauthenticated
            ),
            userPreferencesManager = FakeUserPreferencesManager()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        assertEquals(UserState.Unauthenticated(hasSkippedOnboarding = false),
            viewModel.userState.value
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initViewModel should update user state when no session is found and user has skipped onboarding`() = runTest {
        viewModel = LoginViewModel(
            authRepo = FakeAuthRepository(
                errorScenario = AuthScenario.UnauthenticatedSkipOnboarding
            ),
            userPreferencesManager = FakeUserPreferencesManager()
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        assertEquals(UserState.Unauthenticated(hasSkippedOnboarding = true),
            viewModel.userState.value
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `submitLogin should update uiState isLoading to false when error occurs`() = runTest {
        viewModel = LoginViewModel(
            FakeAuthRepository(
                errorScenario = AuthScenario.LoginError
            ),
            userPreferencesManager = FakeUserPreferencesManager()
        )
        viewModel.submitLogin()
        advanceUntilIdle()
        assertEquals(false, viewModel.uiState.value.isLoading)
    }
}