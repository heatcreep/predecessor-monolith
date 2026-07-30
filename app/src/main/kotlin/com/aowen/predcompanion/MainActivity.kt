package com.aowen.predcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.aowen.predcompanion.core.data.repository.auth.NewAuthRepository
import com.aowen.predcompanion.core.data.repository.user.UserRepository
import com.aowen.predcompanion.core.data.repository.user.UserState
import com.aowen.predcompanion.core.datastore.Theme
import com.aowen.predcompanion.core.datastore.ThemePreferences
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.ui.MonolithApp
import com.aowen.predcompanion.ui.SignInScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var newAuthRepository: NewAuthRepository

    @Inject
    lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch { userRepository.sync() }

        setContent {
            val localTheme by themePreferences.theme.collectAsStateWithLifecycle(initialValue = Theme.SYSTEM)
            val userState by userRepository.currentUserState.collectAsStateWithLifecycle(initialValue = UserState.Loading)

            MonolithTheme(localTheme = localTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (userState) {
                        is UserState.Loading -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }

                        is UserState.SignedOut, is UserState.Error -> {
                            SignInScreen(
                                onLoginIntent = { newAuthRepository.loginIntent() },
                                onLoginResult = { data ->
                                    lifecycleScope.launch {
                                        newAuthRepository.onLoginResult(data)
                                        userRepository.sync()
                                    }
                                }
                            )
                        }

                        is UserState.SignedIn -> {
                            MonolithApp()
                        }
                    }
                }
            }
        }
    }
}
