package com.aowen.monolith.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.aowen.monolith.data.datastore.Theme

val DarkColorScheme = darkColorScheme(
    surface = NeroBlack,
    onSurface = LightKhaki,
    primary = NeroBlack,
    secondary = LightKhaki,
    tertiary = WarmWhite,
    primaryContainer = NeroGrey,
    onPrimaryContainer = LightKhaki,
    background = NeroBlack,
    onBackground = NeroGrey,
    secondaryContainer = DiscordBlurple,
    onSecondaryContainer = WarmWhite,
    tertiaryContainer = LightKhaki,
    onTertiary = NeroBlack,
    onTertiaryContainer = DarkKhaki,
    outlineVariant = LightKhaki.copy(alpha = 0.2f),
    inversePrimary = Color.LightGray,
)

val LightColorScheme = lightColorScheme(
    surface = WarmWhite200,
    onSurface = NeroBlack,
    primary = WarmWhite,
    secondary = NeroBlack,
    tertiary = NeroGrey,
    primaryContainer = WarmWhite,
    onPrimaryContainer = NeroBlack,
    background = WarmWhite200,
    secondaryContainer = DiscordBlurple,
    onSecondaryContainer = WarmWhite,
    onTertiary = WarmWhite,
    tertiaryContainer = NeroLightGrey,
    outlineVariant = NeroBlack.copy(0.2f),
    onTertiaryContainer = NeroGrey,

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
    localTheme: Theme = Theme.SYSTEM,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        localTheme != Theme.SYSTEM -> {
            if (localTheme == Theme.DARK) DarkColorScheme else LightColorScheme
        }

        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}