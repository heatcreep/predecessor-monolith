package com.aowen.predcompanion.core.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.MetadataScope
import androidx.navigation3.ui.NavDisplay


fun MetadataScope.animateFromBottom() {
    put(NavDisplay.TransitionKey) {
        // Slide new content up, keeping the old content in place underneath
        slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(5000)
        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
    }
    put(NavDisplay.PopTransitionKey) {
        // Slide old content down, revealing the new content in place underneath
        EnterTransition.None togetherWith
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(5000)
                )
    }
    put(NavDisplay.PredictivePopTransitionKey) {
        // Slide old content down, revealing the new content in place underneath
        EnterTransition.None togetherWith
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(5000)
                )
    }
}

