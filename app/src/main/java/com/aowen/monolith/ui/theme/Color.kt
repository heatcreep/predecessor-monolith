package com.aowen.monolith.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Blacks
val NeroBlack = Color(0xFF1C1C1C)

val NeroGrey = Color(0xFF2C2C2C)

// Browns
val DarkKhaki = Color(0xFF645C45)
val LightKhaki = Color(0xFFE6DEC6)

// Greys
val WarmGrey = Color(0xFF8B8B8A)
val CoolGrey = Color(0xFF87858A)

// Whites
val WarmWhite = Color(0xFFE3E3E1)
val WarmWhite200 = Color(0xFFEEEEEE)


// Green
val GreenHighlight = Color(0xFF69AF69)
val DarkGreenHighlight = Color(0x561C421C)

// Red
val RedHighlight = Color(0xFFAF6969)
val DarkRedHighlight = Color(0xFF683D3D)

// Blue
val BlueHighlight = Color(0xFF6969AF)

// SOCIAL

// Discord
val DiscordDarkBackground = Color(0xFF36393E)
val DiscordBlurple = Color(0xFF8B9CDE)

@Composable
fun inputFieldDefaults() = TextFieldDefaults.colors(
    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
    cursorColor = MaterialTheme.colorScheme.secondary,
    selectionColors = selectColorDefaults(),
    focusedSupportingTextColor = MaterialTheme.colorScheme.primaryContainer,
    focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
    focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
    focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimaryContainer,
    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimaryContainer,
    focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
)

@Composable
fun selectColorDefaults() = TextSelectionColors(
    backgroundColor = DarkKhaki,
    handleColor = MaterialTheme.colorScheme.secondary
)