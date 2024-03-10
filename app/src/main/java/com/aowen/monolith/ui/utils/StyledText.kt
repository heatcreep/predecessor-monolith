package com.aowen.monolith.ui.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.aowen.monolith.ui.theme.BlueHighlight
import com.aowen.monolith.ui.theme.GreenHighlight
import com.aowen.monolith.ui.theme.ManaBlue
import com.aowen.monolith.ui.theme.OrangeHighlight
import com.aowen.monolith.ui.theme.RedHighlight
import com.aowen.monolith.ui.theme.YellowHighlight

private const val AbililtyPowerText = "AbilityPowerText"
private const val AttackDamageText = "AttackDamageText"
private const val CooldownText = "cd_text"
private const val EffectText = "EffectText"
private const val GoldText = "GoldText"
private const val HealthText = "HealthText"
private const val ManaText = "ManaText"


fun parseText(content: String): List<Triple<String, Boolean, String?>> {
    val result = mutableListOf<Triple<String, Boolean, String?>>()
    // Removing <img> tags, including malformed closing tags by focusing on the opening <img> tag and any form of </img> closing tag.
    val contentWithoutImages = content.replace(Regex("<img.*?>.*?</img.*?>|<img.*?/?>"), "")
    // Handle <br> tags by replacing them with newline characters
    val contentWithLineBreaks = contentWithoutImages.replace("<br>", "\n")
    // Regex to capture more generic tag patterns
    val regex = Regex("<(\\w+)>(.*?)</\\1>")
    var lastIndex = 0

    regex.findAll(contentWithLineBreaks).forEach { matchResult ->
        val tag = matchResult.groupValues[1]
        val startIndex = matchResult.range.first
        val endIndex = matchResult.range.last + 1

        // Add text before the tag as normal text
        if (startIndex > lastIndex) {
            result.add(Triple(contentWithLineBreaks.substring(lastIndex, startIndex), false, null))
        }

        // Add tagged content as styled text
        result.add(Triple(matchResult.groupValues[2], true, tag))

        lastIndex = endIndex
    }

    // Add remaining text as normal text
    if (lastIndex < content.length) {
        result.add(Triple(contentWithLineBreaks.substring(lastIndex), false, null))
    }

    return result
}


@Composable
fun StyledText(content: String) {
    val parsedContent = parseText(content)
    val annotatedString = buildAnnotatedString {
        parsedContent.forEach { (text, _, tag) ->
            when (tag) {
                AbililtyPowerText -> withStyle(
                    style = SpanStyle(
                        color = BlueHighlight,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(text)
                }

                AttackDamageText -> withStyle(
                    style = SpanStyle(
                        color = RedHighlight,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(text)
                }

                CooldownText -> withStyle(
                    style = SpanStyle(
                        color = OrangeHighlight,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(text)
                }

                EffectText -> withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(text)
                }

                GoldText -> withStyle(
                    style = SpanStyle(
                        color = YellowHighlight,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(text)
                }

                HealthText -> withStyle(
                    style = SpanStyle(
                        color = GreenHighlight,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(text)
                }

                ManaText -> withStyle(
                    style = SpanStyle(
                        color = ManaBlue,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(text)
                }

                else -> append(text) // Normal text
            }
        }
    }
    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary
    )
}

