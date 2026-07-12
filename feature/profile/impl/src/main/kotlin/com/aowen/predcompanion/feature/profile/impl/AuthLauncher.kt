package com.aowen.predcompanion.feature.profile.impl

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberAuthLauncher(
    intentProvider: () -> Intent,
    onResult: (Intent?) -> Unit,
): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result -> onResult(result.data) }
    return remember(launcher) { { launcher.launch(intentProvider()) } }
}