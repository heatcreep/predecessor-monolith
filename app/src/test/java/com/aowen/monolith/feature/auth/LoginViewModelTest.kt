package com.aowen.monolith.feature.auth

import com.aowen.monolith.fakes.AuthErrorScenario
import com.aowen.monolith.fakes.AuthTokenScenario
import com.aowen.monolith.fakes.FakeAuthRepository
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
    fun `initViewModel should update uiState isLoading when accessToken is null`() = runTest {
        viewModel = LoginViewModel(FakeAuthRepository(
            errorScenario = AuthErrorScenario.NoCurrentSession
        ))
        viewModel.initViewModel()
        advanceUntilIdle()
        assertEquals(true, viewModel.uiState.value.isLoading)
        assertEquals(UserState.Unauthenticated, viewModel.userState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initViewModel should update uiState isLoading when accessToken is not null`() = runTest {
        viewModel = LoginViewModel(FakeAuthRepository(
            tokenScenario = AuthTokenScenario.ValidAccessToken
        ))
        viewModel.initViewModel()
        advanceUntilIdle()
        val expected = LoginUiState(isLoading = true)
        assertEquals(expected, viewModel.uiState.value)
        assertEquals(UserState.Authenticated, viewModel.userState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initViewModel should set error message when no user is found`() = runTest {
        viewModel = LoginViewModel(
            FakeAuthRepository(
                errorScenario = AuthErrorScenario.FailedCurrentSession,
                tokenScenario = AuthTokenScenario.ValidAccessToken
            )
        )
        viewModel.initViewModel()
        advanceUntilIdle()
        assertEquals(false, viewModel.uiState.value.isLoading)
        assertEquals("There was an issue signing you in. Please try again.", viewModel.uiState.value.errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `submitLogin should update uiState isLoading to false when finished`() = runTest {
        viewModel = LoginViewModel(FakeAuthRepository())
        viewModel.submitLogin()
        advanceUntilIdle()
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `submitLogin should update uiState isLoading to false when error occurs`() = runTest {
        viewModel = LoginViewModel(FakeAuthRepository(
            errorScenario = AuthErrorScenario.LoginError
        ))
        viewModel.submitLogin()
        advanceUntilIdle()
        assertEquals(false, viewModel.uiState.value.isLoading)
    }
}