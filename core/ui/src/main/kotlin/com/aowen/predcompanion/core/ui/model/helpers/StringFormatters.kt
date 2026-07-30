package com.aowen.predcompanion.core.ui.model.helpers

fun Float.toPercentageString(decimals: Int = 0): String {
    return "%.${decimals}f%%".format(this * 100)
}