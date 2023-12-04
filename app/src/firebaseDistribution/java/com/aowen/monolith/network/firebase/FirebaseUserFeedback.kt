package com.aowen.monolith.network.firebase

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.aowen.monolith.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.Firebase
import com.google.firebase.appdistribution.InterruptionLevel
import com.google.firebase.appdistribution.appDistribution

class FirebaseUserFeedback : UserFeedback {
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun Feedback() {
        val permissionsState =
            rememberPermissionState(
                permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.POST_NOTIFICATIONS
                } else ""
            )

        val context = LocalContext.current
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        val rejectedPermission = rememberSaveable {
            mutableStateOf(sharedPreferences.getBoolean("rejected_permission", false))
        }

        val showDialog = rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            if (!permissionsState.status.isGranted
                && !rejectedPermission.value
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            ) {
                showDialog.value = true
            }
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Firebase.appDistribution.showFeedbackNotification(
                    // Text providing notice to your testers about collection and processing of their feedback data
                    R.string.additionalFormText,
                    // The level of interruption for the notification
                    InterruptionLevel.DEFAULT
                )
            } else {
                rejectedPermission.value = true
                sharedPreferences.edit().putBoolean("rejected_permission", true).apply()
                Toast.makeText(context, R.string.feedback_deny_toast, Toast.LENGTH_LONG).show()
            }
        }

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                text = {
                    Text(
                        "This app needs notification permission to allow for Firebase Tester Feedback.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog.value = false
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text("Allow")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog.value = false
                            rejectedPermission.value = true
                            sharedPreferences.edit().putBoolean("rejected_permission", true).apply()
                            Toast.makeText(context, R.string.feedback_deny_toast, Toast.LENGTH_LONG)
                                .show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text("Deny")
                    }
                }
            )
        }
    }
}