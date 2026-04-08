package com.aowen.predcompanion.core.model.network

sealed interface UserState {
    object Loading : UserState
    data class Unauthenticated(val hasSkippedOnboarding: Boolean) : UserState
    object Authenticated : UserState
}
