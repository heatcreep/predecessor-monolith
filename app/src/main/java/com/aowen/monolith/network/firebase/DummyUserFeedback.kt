package com.aowen.monolith.network.firebase

import androidx.compose.runtime.Composable

class DummyUserFeedback: UserFeedback {
    @Composable
    override fun Feedback() {
        /* no-op */
    }
}