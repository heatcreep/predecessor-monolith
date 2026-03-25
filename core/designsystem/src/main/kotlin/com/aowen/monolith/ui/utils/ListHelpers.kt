package com.aowen.monolith.ui.utils

fun <T> List<T>.filterOrOriginal(predicate: (T) -> Boolean) : List<T> =
    this.filter(predicate).takeUnless { it.isEmpty() } ?: this@filterOrOriginal