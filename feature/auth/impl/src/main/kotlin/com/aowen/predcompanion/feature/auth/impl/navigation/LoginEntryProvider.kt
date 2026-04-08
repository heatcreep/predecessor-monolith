package com.aowen.predcompanion.feature.auth.impl.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.aowen.predcompanion.core.model.network.UserState
import com.aowen.predcompanion.feature.auth.api.navigation.AuthNavKey
import com.aowen.predcompanion.feature.auth.impl.LoginScreen
import com.aowen.predcompanion.feature.auth.impl.LoginViewModel
import com.aowen.predcompanion.logDebug

fun EntryProviderScope<NavKey>.loginEntry() {
    entry<AuthNavKey> {
        val viewModel = hiltViewModel<LoginViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val userState by viewModel.userState.collectAsStateWithLifecycle()

        LaunchedEffect(userState) {
            when (userState) {
                is UserState.Loading -> {
                    logDebug("Loading user state", "LoginScreen")
                }
                is UserState.Authenticated -> {
                    // Session transition is handled at the Activity level; nothing to do here.
                }
                is UserState.Unauthenticated -> {
                    logDebug("User is not signed in", "LoginScreen")
                    viewModel.setLoading(false)
                }
            }
        }
        LoginScreen(
            uiState = uiState,
            submitLogin = viewModel::submitLogin,
            handleSkipOnboarding = viewModel::handleSkipOnboarding
        )
    }
}
