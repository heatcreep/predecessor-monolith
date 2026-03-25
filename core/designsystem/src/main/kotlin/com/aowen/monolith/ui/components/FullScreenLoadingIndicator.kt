package com.aowen.monolith.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
