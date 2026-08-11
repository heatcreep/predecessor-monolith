package com.aowen.predcompanion.core.ui.utils

fun <T> Collection<T>.filterOrOriginal(predicate: (T) -> Boolean) : List<T> =
    this.toList().filter(predicate).takeUnless { it.isEmpty() } ?: this@filterOrOriginal.toList()