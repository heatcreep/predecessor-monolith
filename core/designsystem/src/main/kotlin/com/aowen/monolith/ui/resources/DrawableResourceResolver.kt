package com.aowen.monolith.ui.resources

import android.content.Context
import androidx.annotation.DrawableRes

/**
 * Resolves drawable string keys to actual R.drawable resource IDs.
 * This decouples model/data classes from Android resource references,
 * allowing them to live in pure Kotlin modules.
 */
object DrawableResourceResolver {

    /**
     * Resolve a drawable key (e.g. "akeron", "bronze_200") to its drawable resource ID.
     * Returns the "unknown" drawable if the key is not found.
     */
    @DrawableRes
    fun resolve(context: Context, drawableKey: String): Int {
        val resId = context.resources.getIdentifier(drawableKey, "drawable", context.packageName)
        return if (resId != 0) resId else {
            context.resources.getIdentifier("unknown", "drawable", context.packageName)
        }
    }
}
