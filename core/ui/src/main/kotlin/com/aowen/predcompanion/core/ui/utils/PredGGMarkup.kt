package com.aowen.predcompanion.core.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

data class ImageAltInfo(val text: String, val styleKey: String? = null)

sealed class MarkupNode {
    data class Text(val text: String) : MarkupNode()
    data class Tag(val name: String, val children: List<MarkupNode>) : MarkupNode()
    data class Image(val id: String) : MarkupNode()
}

object PredGGMarkup {
    private val tagRegex = Regex("<(\\w+)>(.*?)</>", RegexOption.DOT_MATCHES_ALL)
    private val imgRegex = Regex("""<img\s+id="(\w+)"\s*/?>(?:</>)?""")

    fun parseMarkup(input: String): List<MarkupNode> {
        val nodes = mutableListOf<MarkupNode>()
        var lastIndex = 0

        data class MatchInfo(val range: IntRange, val node: MarkupNode)

        val matches = mutableListOf<MatchInfo>()

        for (match in tagRegex.findAll(input)) {
            val (tagName, inner) = match.destructured
            matches += MatchInfo(match.range, MarkupNode.Tag(tagName, parseMarkup(inner)))
        }

        for (match in imgRegex.findAll(input)) {
            val (imgId) = match.destructured
            matches += MatchInfo(match.range, MarkupNode.Image(imgId))
        }

        matches.sortBy { it.range.first }

        for (matchInfo in matches) {
            if (matchInfo.range.first < lastIndex) continue
            if (matchInfo.range.first > lastIndex) {
                nodes += MarkupNode.Text(input.substring(lastIndex, matchInfo.range.first))
            }
            nodes += matchInfo.node
            lastIndex = matchInfo.range.last + 1
        }

        if (lastIndex < input.length) {
            nodes += MarkupNode.Text(input.substring(lastIndex))
        }
        return nodes
    }

    fun buildMarkupAnnotatedString(
        nodes: List<MarkupNode>,
        styles: Map<String, SpanStyle>,
        imageAltText: Map<String, ImageAltInfo> = emptyMap()
    ): AnnotatedString = buildAnnotatedString {
        fun visit(nodes: List<MarkupNode>) {
            for (node in nodes) when (node) {
                is MarkupNode.Text -> append(node.text)
                is MarkupNode.Tag -> withStyle(styles[node.name] ?: SpanStyle(Color(0xFFd2ff00))) {
                    visit(node.children)
                }
                is MarkupNode.Image -> {
                    val info = imageAltText[node.id]
                    if (info != null) {
                        val style = info.styleKey?.let { styles[it] }
                        if (style != null) {
                            withStyle(style) { append(info.text) }
                        } else {
                            append(info.text)
                        }
                    }
                }
            }
        }
        visit(nodes)
    }
}
