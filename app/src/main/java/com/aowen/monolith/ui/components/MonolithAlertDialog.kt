package com.aowen.monolith.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aowen.monolith.ui.theme.MonolithTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonolithAlertDialog(
    bodyText: String,
    cancelText: String = "Cancel",
    confirmText: String = "Confirm",
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(
                text = bodyText,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.size(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onDismissRequest) {
                    Text(cancelText, color = MaterialTheme.colorScheme.secondary)
                }
                TextButton(onClick = onConfirm) {
                    Text(confirmText, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AlertDialogPreview() {
    MonolithTheme {
        MonolithAlertDialog(
            bodyText = "Are you sure you want to clear all recent searches? This action cannot be undone.",
            confirmText = "Clear all",
            onDismissRequest = {},
            onConfirm = {}
        )
    }
}