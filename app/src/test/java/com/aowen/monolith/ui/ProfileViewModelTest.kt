package com.aowen.monolith.ui

import com.aowen.monolith.fakes.FakeUserRepository
import com.aowen.monolith.fakes.data.fakeUserInfo
import com.aowen.monolith.ui.screens.profile.ProfileScreenUiState
import com.aowen.monolith.ui.screens.profile.ProfileViewModel
import com.aowen.monolith.utils.MainDispatcherRule
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
            userRepository = FakeUserRepository()
        )
    }

    @Test
    fun `when initViewModel is called, then uiState is updated with userInfo`() {

        val expected = ProfileScreenUiState(
            isLoading = false,
            error = null,
            userInfo = fakeUserInfo
        )
        val actual = viewModel.uiState.value

        assertEquals(expected, actual)
    }

    @Test
    fun `when initViewModel is called, then uiState is error when getUser returns null`() {

        viewModel = ProfileViewModel(
            userRepository = FakeUserRepository(error = true)
        )

        val expected = ProfileScreenUiState(
            isLoading = false,
            error = "Error getting user info."
        )
        val actual = viewModel.uiState.value

        assertEquals(expected, actual)
    }

    @Test
    fun `handleLogout() calls userRepository logout`() {
        val userRepository = FakeUserRepository()
        viewModel = ProfileViewModel(
            userRepository = userRepository
        )

        assertTrue(userRepository.logoutCounter.value == 0)

        viewModel.handleLogout()

        val expected = 1
        val actual = userRepository.logoutCounter.value

        assertEquals(expected, actual)
    }
}