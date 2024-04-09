package com.aowen.monolith.glance.utils

import android.graphics.BitmapFactory
import androidx.core.net.toUri
import androidx.glance.ImageProvider
import androidx.glance.appwidget.ImageProvider

fun getImageProvider(path: String): ImageProvider {
    if (path.startsWith("content://")) {
        return ImageProvider(path.toUri())
    }
    val bitmap = BitmapFactory.decodeFile(path)
    return ImageProvider(bitmap)
}