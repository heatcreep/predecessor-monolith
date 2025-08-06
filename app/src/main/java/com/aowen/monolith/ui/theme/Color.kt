package com.aowen.monolith.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Blacks
val NeroBlack = Color(0xFF1C1C1C)

val NeroGrey = Color(0xFF2C2C2C)
val NeroLightGrey = Color(0xFF797979)

// Browns
val DarkKhaki = Color(0xFFA39D8C)
val LightKhaki = Color(0xFFE6DEC6)
val Dawn = Color(0xFF966426)

// Whites
val WarmWhite = Color(0xFFE3E3E1)
val WarmWhite200 = Color(0xFFEEEEEE)


// Green
val GreenHighlight = Color(0xFF69AF69)
val DarkGreenHighlight = Color(0xFF317231)
val DarkGreenHighlight35 = Color(0x4D1C421C)
val BadgeBlueGreen = Color(0xFF34B9AB)

// Red
val RedHighlight = Color(0xFFAF6969)
val DarkRedHighlight = Color(0xFF683D3D)
val RedDanger = Color(0xFF9E0404)

// Orange
val OrangeHighlight = Color(0xFFAF8769)

// Yellow
val YellowHighlight = Color (0xFFAFAF69)

// Blue
val BlueHighlight = Color(0xFF6969AF)
val Dusk = Color(0xFF22308D)
val ManaBlue = Color(0xFF69AFA8)
val BadgePurple = Color(0xFF7D2387)

// SOCIAL
// Discord

val DiscordDarkBackground = Color(0xFF36393E)
val DiscordBlurple = Color(0xFF444CB9)

// RANKS
val Bronze = Color(0xFFA9897A)
val Silver = Color(0xFFACABA5)
val Gold = Color(0xFFA9A77A)
val Platinum =  Color(0xFF13BBC0)
val Diamond = Color(0xFF7A84A9)
val Paragon = Color(0xFFB36E6C)

@Composable
fun inputFieldDefaults() = TextFieldDefaults.colors(
    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
    unfocusedIndicatorColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
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
fun dropDownDefaults() = MenuDefaults.itemColors(
    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
)

@Composable
fun selectColorDefaults() = TextSelectionColors(
    backgroundColor = DarkKhaki,
    handleColor = MaterialTheme.colorScheme.secondary
)