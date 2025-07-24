package com.aowen.monolith.feature.auth

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions
import com.aowen.monolith.R
import com.aowen.monolith.feature.home.navigation.navigateToHome
import com.aowen.monolith.logDebug
import com.aowen.monolith.network.UserState
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.WarmWhite

@Composable
internal fun LoginRoute(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState(LoginUiState())
    val userState = viewModel.userState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.initViewModel()
    }

    LaunchedEffect(userState) {
        when(userState) {
            UserState.Loading -> {
                logDebug("Loading user state", "LoginScreen")
            }
            UserState.Authenticated -> {
                navController.navigateToHome(navOptions {
                    launchSingleTop = true
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                })
            }
            is UserState.Unauthenticated -> {
                if(userState.hasSkippedOnboarding) {
                    navController.navigateToHome()
                }
                logDebug("User is not signed in", "LoginScreen")
                viewModel.setLoading(false)
            }
        }
    }

    LoginScreen(
        modifier = modifier,
        uiState = uiState,
        submitLogin = viewModel::submitLogin,
        handleSkipOnboarding = {
            viewModel.handleSkipOnboarding()
            navController.navigateToHome()
        }
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    submitLogin: () -> Unit = {},
    handleSkipOnboarding: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.login_bg),
                alignment = Alignment.BottomEnd,
                alpha = 0.2f,
                contentScale = ContentScale.Crop
            )
            .padding(horizontal = 16.dp),
    ) {
        if(uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.size(40.dp))
                SignInDiscordButton(
                    submitLogin = submitLogin
                )
                uiState.errorMessage?.let { errorMessage ->
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        text = errorMessage,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                TextButton(
                    onClick = handleSkipOnboarding
                ) {
                    Text(
                        text = "Skip for now",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "Powered by Omeda.city",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
fun SignInDiscordButton(
    modifier: Modifier = Modifier,
    submitLogin: () -> Unit = {}
) {

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = WarmWhite
    )

    ElevatedButton(
        onClick = submitLogin,
        modifier = modifier,
        colors = buttonColors,
        contentPadding = PaddingValues(24.dp)
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(id = R.drawable.discord_mark_white),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = "Sign in with Discord"
        )
    }

}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LoginScreenPreview(
    @PreviewParameter(LoginScreenPreviewParameterProvider::class) previewState: LoginUiState
) {
    MonolithTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            LoginScreen(uiState = previewState)
        }
    }
}

