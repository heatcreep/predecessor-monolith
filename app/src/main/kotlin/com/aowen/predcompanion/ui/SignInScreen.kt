package com.aowen.predcompanion.ui

import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.component1
import androidx.activity.result.component2
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aowen.predcompanion.R
import com.aowen.predcompanion.ui.theme.WarmWhite
import kotlinx.coroutines.launch
import com.aowen.predcompanion.core.resources.R as coreResources

@Composable
fun SignInScreen(
    onLoginIntent: () -> Intent,
    onLoginResult: (Intent) -> Unit,
    onLoginCancelled: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val signInCancelledMessage = stringResource(R.string.app_sign_in_cancel)
    val signInErrorMessage = stringResource(R.string.app_sign_in_error)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val (resultCode, data) = result

        when {
            resultCode == RESULT_CANCELED -> {
                onLoginCancelled()
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = signInCancelledMessage
                    )
                }
            }

            data != null -> onLoginResult(data)
            else -> {
                onLoginCancelled()
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = signInErrorMessage
                    )
                }
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.pred_background),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                colorFilter = ColorFilter.tint(
                    color = Color.Black.copy(alpha = 0.5f),
                    blendMode = BlendMode.Darken
                ),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(coreResources.string.core_resources_app_name),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(modifier = Modifier.size(32.dp))
                ElevatedButton(
                    onClick = {
                        launcher.launch(onLoginIntent())
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = WarmWhite
                    ),
                    contentPadding = PaddingValues(24.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        tint = Color.Unspecified,
                        painter = painterResource(id = coreResources.drawable.predgg_icon_only),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = stringResource(R.string.app_sign_in_button_text),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
    }
}
