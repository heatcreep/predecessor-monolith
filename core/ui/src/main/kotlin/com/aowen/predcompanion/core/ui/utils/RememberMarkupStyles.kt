package com.aowen.predcompanion.core.ui.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.aowen.predcompanion.ui.theme.AbilityTextPurple
import com.aowen.predcompanion.ui.theme.AttackDamageRed
import com.aowen.predcompanion.ui.theme.CritChanceText
import com.aowen.predcompanion.ui.theme.DebuffPink
import com.aowen.predcompanion.ui.theme.GoldText
import com.aowen.predcompanion.ui.theme.GreenHighlight
import com.aowen.predcompanion.ui.theme.ManaBlue
import com.aowen.predcompanion.ui.theme.Silver

val LocalMarkupStyles = staticCompositionLocalOf<Map<String, SpanStyle>> { emptyMap() }
val LocalMarkupImageAltText = staticCompositionLocalOf<Map<String, ImageAltInfo>> { emptyMap() }

@Composable
fun rememberMarkupImageAltText(): Map<String, ImageAltInfo> {
    return remember {
        mapOf(
            "ADIconOrange" to ImageAltInfo(text = "Physical Power", styleKey = "AttackDamageText"),
            "APIconBlue" to ImageAltInfo(text = "Ability Power", styleKey = "AbilityPowerText")
        )
    }
}

@Composable
fun rememberMarkupStyles(): Map<String, SpanStyle> {
    val colors = MaterialTheme.colorScheme
    return remember(colors) {
        mapOf(
            "AbilityName" to AbilityTextPurple,
            "AbilityPowerText" to ManaBlue,
            "ArmorText" to Silver,
            "AttackDamageText" to AttackDamageRed,
            "CC_Text" to DebuffPink,
            "Condition" to Color.White,
            "CritChanceText" to CritChanceText,
            "EffectText" to Color.White,
            "GoldText" to GoldText,
            "HealthText" to GreenHighlight,
            "ManaText" to ManaBlue,
            "PenDamageText" to AbilityTextPurple,
            "RageText" to colors.tertiary,
            "ShieldText" to Color.White,
        ).mapValues { (label, color) ->
            SpanStyle(
                color = color,
                fontWeight = if (label == "Condition") FontWeight.ExtraLight else FontWeight.ExtraBold,
                fontStyle = if (label == "Condition") FontStyle.Italic else null
            )
        }
    }
}