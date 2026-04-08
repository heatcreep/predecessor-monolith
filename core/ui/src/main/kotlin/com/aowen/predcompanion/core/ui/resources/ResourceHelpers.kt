package com.aowen.predcompanion.core.ui.resources

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * Resolve a drawable string key to a drawable resource ID at composable scope.
 */
@Composable
@DrawableRes
fun drawableKey(key: String): Int {
    val context = LocalContext.current
    return DrawableResourceResolver.resolve(context, key)
}

/**
 * Resolve a rank color key to a Compose Color.
 * The rank color keys match: "Bronze", "Silver", "Gold", "Platinum", "Diamond", "Paragon", "LightKhaki"
 */
fun rankColorFromKey(colorKey: String): Color {
    return when (colorKey) {
        "Bronze" -> Color(0xFFA9897A)
        "Silver" -> Color(0xFFACABA5)
        "Gold" -> Color(0xFFA9A77A)
        "Platinum" -> Color(0xFF13BBC0)
        "Diamond" -> Color(0xFF7A84A9)
        "Paragon" -> Color(0xFFB36E6C)
        "LightKhaki" -> Color(0xFFE6DEC6)
        else -> Color(0xFFE6DEC6)
    }
}

/**
 * Resolve a drawable key using Context (for non-composable code).
 */
@DrawableRes
fun resolveDrawable(context: Context, key: String): Int {
    return DrawableResourceResolver.resolve(context, key)
}
