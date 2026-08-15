package com.aowen.predcompanion.core.ui.shared

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import com.aowen.predcompanion.core.ui.utils.ImageAltInfo
import com.aowen.predcompanion.core.ui.utils.LocalMarkupImageAltText
import com.aowen.predcompanion.core.ui.utils.LocalMarkupStyles
import com.aowen.predcompanion.core.ui.utils.PredGGMarkup.buildMarkupAnnotatedString
import com.aowen.predcompanion.core.ui.utils.PredGGMarkup.parseMarkup

@Composable
fun PredGGMarkupText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = MaterialTheme.colorScheme.secondary,
    markupStyles: Map<String, SpanStyle> = LocalMarkupStyles.current,
    imageAltText: Map<String, ImageAltInfo> = LocalMarkupImageAltText.current,
) {
    val annotated = remember(text, markupStyles, imageAltText) {
        buildMarkupAnnotatedString(parseMarkup(text), markupStyles, imageAltText)
    }
    Text(text = annotated, style = style, color = color, modifier = modifier)
}