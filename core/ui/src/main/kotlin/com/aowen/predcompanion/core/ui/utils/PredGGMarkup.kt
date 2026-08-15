package com.aowen.predcompanion.core.ui.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

sealed class MarkupNode {
    data class Text(val text: String) : MarkupNode()
    data class Tag(val name: String, val children: List<MarkupNode>) : MarkupNode()
}

object PredGGMarkup {
    private val tagRegex = Regex("<(\\w+)>(.*?)</>", RegexOption.DOT_MATCHES_ALL)
    fun parseMarkup(input: String): List<MarkupNode> {
        val nodes = mutableListOf<MarkupNode>()
        var lastIndex = 0

        for (match in tagRegex.findAll(input)) {
            if (match.range.first > lastIndex) {
                nodes += MarkupNode.Text(input.substring(lastIndex, match.range.first))
            }
            val (tagName, inner) = match.destructured
            nodes += MarkupNode.Tag(tagName, parseMarkup(inner)) // recursion = nested tags for free
            lastIndex = match.range.last + 1
        }
        if (lastIndex < input.length) {
            nodes += MarkupNode.Text(input.substring(lastIndex))
        }
        return nodes
    }

    fun buildMarkupAnnotatedString(
        nodes: List<MarkupNode>,
        styles: Map<String, SpanStyle>
    ): AnnotatedString = buildAnnotatedString {
        fun visit(nodes: List<MarkupNode>) {
            for (node in nodes) when (node) {
                is MarkupNode.Text -> append(node.text)
                is MarkupNode.Tag -> withStyle(styles[node.name] ?: SpanStyle(Color(0xFFd2ff00))) {
                    visit(node.children)
                }
            }
        }
        visit(nodes)
    }
}

