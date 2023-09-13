package com.aowen.monolith.network.utils

fun String.trimExtraNewLine() = this
    .replace("\n\n", "")
    .replace("\r\n\r\n", "\n")