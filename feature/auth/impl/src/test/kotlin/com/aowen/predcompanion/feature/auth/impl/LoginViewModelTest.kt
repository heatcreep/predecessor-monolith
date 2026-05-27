package com.aowen.predcompanion.feature.auth.impl

import com.aowen.predcompanion.core.network.model.NetworkUserState
import com.aowen.predcompanion.core.testing.fakes.preferences.FakeUserPreferencesManager
import com.aowen.predcompanion.core.testing.fakes.repository.AuthScenario
import com.aowen.predcompanion.core.testing.fakes.repository.FakeAuthRepository
import com.aowen.predcompanion.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

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
        advanceUntilIdle()
        Assert.assertEquals(
            NetworkUserState.Authenticated,
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
        advanceUntilIdle()
        Assert.assertEquals(
            NetworkUserState.Unauthenticated(hasSkippedOnboarding = false),
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
        advanceUntilIdle()
        Assert.assertEquals(
            NetworkUserState.Unauthenticated(hasSkippedOnboarding = true),
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
        Assert.assertEquals(false, viewModel.uiState.value.isLoading)
    }
}