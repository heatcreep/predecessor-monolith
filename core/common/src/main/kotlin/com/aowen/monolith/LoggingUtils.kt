package com.aowen.monolith

import android.util.Log

const val LOG_TAG = "MONOLITH_DEBUG: "

fun logDebug(message: String, tag: String? = LOG_TAG) {
    Log.d(tag, message)
}