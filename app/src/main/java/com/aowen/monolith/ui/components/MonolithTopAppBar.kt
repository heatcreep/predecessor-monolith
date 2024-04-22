package com.aowen.monolith.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonolithTopAppBar(
    title: String,
    titleStyle: TextStyle? = null,
    backAction: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
) {
    TopAppBar(
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = {
            Text(
                text = title,
                style = titleStyle ?: MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        navigationIcon = {
            backAction?.invoke()
        },
        actions = {
            actions?.invoke()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
        )
    )
}