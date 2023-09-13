package com.aowen.monolith.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NeroBlack,
    secondary = LightKhaki,
    tertiary = WarmWhite,
    primaryContainer = NeroGrey,
    onPrimaryContainer = LightKhaki,
    background = NeroBlack,
    secondaryContainer = DiscordBlurple,
    onSecondaryContainer = WarmWhite,
    tertiaryContainer = LightKhaki,
    onTertiaryContainer = NeroBlack,
)

private val LightColorScheme = lightColorScheme(
    primary = WarmWhite,
    secondary = NeroBlack,
    tertiary = NeroGrey,
    primaryContainer = WarmWhite,
    onPrimaryContainer = NeroBlack,
    background = WarmWhite200,
    secondaryContainer = DiscordBlurple,
    onSecondaryContainer = WarmWhite,
    tertiaryContainer = NeroGrey,
    onTertiaryContainer = LightKhaki

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MonolithTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}