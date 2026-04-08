package com.aowen.predcompanion.feature.auth.impl

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
import com.aowen.predcompanion.core.designsystem.MonolithTheme
import com.aowen.predcompanion.ui.theme.WarmWhite
import com.aowen.predcompanion.core.resources.R as coreResources


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
                painter = painterResource(id = coreResources.drawable.login_bg),
                alignment = Alignment.BottomEnd,
                alpha = 0.2f,
                contentScale = ContentScale.Crop
            )
            .padding(horizontal = 16.dp),
    ) {
        if (uiState.isLoading) {
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
                    text = stringResource(id = coreResources.string.core_resources_app_name),
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
            painter = painterResource(id = coreResources.drawable.discord_mark_white),
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
