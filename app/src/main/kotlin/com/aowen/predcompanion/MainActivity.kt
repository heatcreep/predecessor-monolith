package com.aowen.predcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.aowen.predcompanion.core.data.repository.auth.AuthRepository
import com.aowen.predcompanion.core.data.repository.user.UserRepository
import com.aowen.predcompanion.core.datastore.Theme
import com.aowen.predcompanion.core.datastore.ThemePreferences
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.feature.auth.api.navigation.AuthNavKey
import com.aowen.predcompanion.feature.auth.impl.navigation.loginEntry
import com.aowen.predcompanion.ui.MonolithApp
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.handleDeeplinks
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var supabaseClient: SupabaseClient

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        supabaseClient.handleDeeplinks(
            intent = intent,
            onSessionSuccess = { userSession ->
                userSession.user?.apply {
                    lifecycleScope.launch {
                        authRepository.handleSuccessfulLoginFromDiscord()
                    }
                }
            },
            onError = { error ->
                // Handle error
                logDebug(error.toString())
            }
        )

        lifecycleScope.launch {
            // Sync user data after session status is checked
            userRepository.sync()
        }

        setContent {
            val localTheme by themePreferences.theme.collectAsStateWithLifecycle(initialValue = Theme.SYSTEM)

            MonolithTheme(localTheme = localTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   MonolithApp()
                }
            }
        }
    }
}

@Composable
private fun UnauthenticatedRoot() {
    val backStack = rememberNavBackStack(AuthNavKey)
    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider { loginEntry() }
    )
}
