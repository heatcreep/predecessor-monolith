package com.aowen.monolith.ui.screens.profile

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.R
import com.aowen.monolith.data.UserInfo
import com.aowen.monolith.navigation.navigateToLoginFromLogout
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.ui.theme.DiscordBlurple
import com.aowen.monolith.ui.theme.DiscordDarkBackground
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.WarmWhite

@Composable
fun ProfileScreenRoute(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    ProfileScreen(
        uiState = uiState,
        handleRetry = viewModel::initViewModel,
        onLogout = viewModel::handleLogout,
        modifier = modifier,
        navigateToLogin = navController::navigateToLoginFromLogout
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileScreenUiState,
    modifier: Modifier = Modifier,
    handleRetry: () -> Unit,
    onLogout: () -> Unit,
    navigateToLogin: () -> Unit,
) {

    val context = LocalContext.current

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isLoading) {
            FullScreenLoadingIndicator("Profile")
        } else {
            if(uiState.error != null) {
                FullScreenErrorWithRetry(
                    errorMessage = uiState.error
                ) {
                    handleRetry()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if(uiState.userInfo != null) {
                        Surface(shape = RoundedCornerShape(5.dp)) {
                            ProfileCard(userInfo = uiState.userInfo)
                        }
                        TextButton(onClick = {
                            onLogout()
                            navigateToLogin()
                            Toast.makeText(
                                context,
                                "logged out successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }) {
                            Text(
                                text = "Sign Out",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                        ) {
                            Text(text = "Sorry, there was an error retrieving your profile. Please try again later.")
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun ProfileCard(
    userInfo: UserInfo
) {

    val context = LocalContext.current

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DiscordBlurple)
                .padding(
                    24.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "signed in with Discord",
                    style = MaterialTheme.typography.titleSmall,
                    color = WarmWhite,
                )
                Text(
                    text = userInfo.fullName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = WarmWhite,
                )
            }
            val model = ImageRequest.Builder(context)
                .data(userInfo.avatarUrl)
                .placeholder(R.drawable.unknown)
                .crossfade(true)
                .build()
            SubcomposeAsyncImage(
                modifier = Modifier.size(64.dp),
                model = model,
                contentDescription = null
            ) {
                SubcomposeAsyncImageContent()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DiscordDarkBackground)
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.bodySmall,
                    color = WarmWhite,
                )
                Text(
                    text = userInfo.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmWhite,
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ProfileCardPreview() {
    MonolithTheme {

    }
}