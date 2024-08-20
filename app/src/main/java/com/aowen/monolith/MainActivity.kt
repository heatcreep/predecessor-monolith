package com.aowen.monolith

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.aowen.monolith.network.AuthRepository
import com.aowen.monolith.ui.MonolithApp
import com.aowen.monolith.ui.theme.MonolithTheme
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.handleDeeplinks
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var supabaseClient: SupabaseClient

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        supabaseClient.handleDeeplinks(intent = intent,
            onSessionSuccess = { userSession ->
                userSession.user?.apply {
                    lifecycleScope.launch {
                        authRepository.saveAccessTokenOnSuccessfulLogin()
                    }
                }
            }
        )
        setContent {
            MonolithTheme {
                // A surface container using the 'background' color from the theme
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
fun FullScreenLoadingIndicator(loadingText: String? = null) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(96.dp),
            color = MaterialTheme.colorScheme.tertiary,
            strokeWidth = 8.dp
        )
        loadingText?.let {
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Loading $it...",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
