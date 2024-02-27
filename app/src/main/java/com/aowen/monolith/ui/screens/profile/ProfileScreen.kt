package com.aowen.monolith.ui.screens.profile

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.aowen.monolith.BuildConfig
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.R
import com.aowen.monolith.data.UserInfo
import com.aowen.monolith.navigation.navigateToLoginFromLogout
import com.aowen.monolith.ui.components.FullScreenErrorWithRetry
import com.aowen.monolith.ui.components.MonolithAlertDialog
import com.aowen.monolith.ui.theme.DiscordBlurple
import com.aowen.monolith.ui.theme.DiscordDarkBackground
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.RedDanger
import com.aowen.monolith.ui.theme.WarmWhite
import kotlinx.coroutines.launch

@Composable
fun ProfileScreenRoute(
    navController: NavController,
    showSnackbar: (String, SnackbarDuration) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    // Show toast for delete account
    LaunchedEffect(true) {
        viewModel.showProfileToast.collect { show ->
            when (show) {
                ProfileToastState.DELETE -> {
                    showSnackbar("Account deleted successfully", SnackbarDuration.Short)
                    navController.navigateToLoginFromLogout()
                }

                ProfileToastState.LOGOUT -> {
                    showSnackbar("Successfully logged out", SnackbarDuration.Short)
                    navController.navigateToLoginFromLogout()
                }

                ProfileToastState.ERROR -> {
                    showSnackbar(
                        "There was an issue processing your request. Please try again later",
                        SnackbarDuration.Short
                    )
                }

                else -> {}
            }
            viewModel.onShowToastComplete()
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        ProfileScreen(
            uiState = uiState,
            modifier = Modifier.padding(it),
            handleRetry = viewModel::initViewModel,
            onLogout = viewModel::handleLogout,
            onDelete = viewModel::deleteUserAccount,
        )
    }

}

@Composable
fun ProfileScreen(
    uiState: ProfileScreenUiState,
    modifier: Modifier = Modifier,
    handleRetry: () -> Unit,
    onLogout: () -> Unit,
    onDelete: () -> Unit,
) {

    val uriHandler = LocalUriHandler.current

    var deleteModalOpen by remember { mutableStateOf(false) }

    if (deleteModalOpen) {
        MonolithAlertDialog(
            bodyText = "Are you sure you want to delete your account? This action is irreversible.",
            confirmText = "Delete",
            cancelText = "Cancel",
            onConfirm = {
                onDelete()
                deleteModalOpen = false
            },
            onDismissRequest = { deleteModalOpen = false },
        )
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isLoading) {
            FullScreenLoadingIndicator("Profile")
        } else {
            if (uiState.error != null) {
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
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    if (uiState.userInfo != null) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Surface(shape = RoundedCornerShape(5.dp)) {
                                ProfileCard(userInfo = uiState.userInfo)
                            }
                            Text(
                                text = "FAQ",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            FaqPanel(
                                questionText = "How does the \"Claim Player\" button work?",
                                answerText = """
                                    The "Claim Player" button allows you to claim a player's profile as your own. However, due to limitations 
                                    of the OmedaCity API, it isn't actually tied to your game account or your OmedaCity account. Think of it as a favorites menu of
                                    one that serves as a convenient way to access your own stats without having to search for yourself everytime.",
                                """.trimIndent()
                            )
                            FaqPanel(
                                questionText = "Why don't I see all the stats that OmedaCity has?",
                                answerText = """
                                    The OmedaCity API exposes a lot of data, but not all of it. Both this app and the OmedaCity API/Website are managed by one person.
                                    We are working to hopefully expose more data in the future, but for now, we are limited to what is available.
                                """.trimIndent()
                            )
                            Text(
                                text = "App Version: ${BuildConfig.VERSION_NAME}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                            ClickableText(
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.secondary,
                                    textDecoration = TextDecoration.Underline
                                ),
                                text = AnnotatedString("Privacy Policy"),
                                onClick = {
                                    uriHandler.openUri("https://monolith-app.dev/privacy")
                                }
                            )
                            ElevatedButton(
                                onClick = { deleteModalOpen = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = RedDanger,
                                    contentColor = WarmWhite
                                )
                            ) {
                                Text(
                                    text = "Delete Account",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            TextButton(onClick = {
                                onLogout()
                            }) {
                                Text(
                                    text = "Sign Out",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
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

@Composable
fun FaqPanel(
    questionText: String,
    answerText: String,
    modifier: Modifier = Modifier
) {

    var expanded by remember { mutableStateOf(false) }
    val rotationAngle = remember { Animatable(0f) }

    LaunchedEffect(expanded) {
        this.launch {
            rotationAngle.animateTo(
                targetValue = if (expanded) 45f else 0f,
                animationSpec = tween(durationMillis = 200, easing = LinearEasing),
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = !expanded
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = questionText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.secondary
            )
            Icon(
                imageVector = Icons.Filled.Add,
                modifier = Modifier
                    .size(28.dp)
                    .rotate(rotationAngle.value),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null
            )

        }
        HorizontalDivider(modifier.padding(vertical = 8.dp))
        AnimatedVisibility(visible = expanded) {
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = answerText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

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
        ProfileScreen(
            uiState = ProfileScreenUiState(
                isLoading = false,
                userInfo = UserInfo(
                    email = "test@gmail.com",
                    avatarUrl = "https://cdn.discordapp.com/avatars/1234567890/abcdef1234567890.png",
                    fullName = "Test User"
                )
            ),
            handleRetry = { /*TODO*/ },
            onLogout = { /*TODO*/ },
            onDelete = { /*TODO*/ }
        )
    }
}