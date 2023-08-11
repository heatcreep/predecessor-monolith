package com.aowen.monolith.ui.screens.profile

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.aowen.monolith.FullScreenLoadingIndicator
import com.aowen.monolith.ui.theme.DiscordBlurple
import com.aowen.monolith.ui.theme.DiscordDarkBackground
import com.aowen.monolith.ui.theme.WarmWhite

@Composable
fun ProfileScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    ProfileScreen(
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileScreenUiState,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        if (uiState.isLoading) {
            FullScreenLoadingIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp    )
            ) {
                Surface(shape = RoundedCornerShape(5.dp)) {
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
                                    text = uiState.userInfo.fullName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = WarmWhite,
                                )
                            }
                            val model = ImageRequest.Builder(context)
                                .data(uiState.userInfo.avatarUrl)
                                .crossfade(true)
                                .build()
                            SubcomposeAsyncImage(
                                modifier = Modifier.size(64.dp),
                                model = model,
                                contentDescription = null
                            ) {
                                val state = painter.state
                                if (state is AsyncImagePainter.State.Success) {
                                    SubcomposeAsyncImageContent()
                                    LaunchedEffect(Unit) {
                                        Log.d("ANDREWO", state.result.dataSource.toString())
                                    }
                                }
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
                                    text = uiState.userInfo.email,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = WarmWhite,
                                )
                            }
                        }
                    }
                }
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = "Sign Out",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}