package com.aowen.predcompanion.core.network.model

sealed interface NetworkUserState {
    object Loading : NetworkUserState
    data class Unauthenticated(val hasSkippedOnboarding: Boolean) : NetworkUserState
    object Authenticated : NetworkUserState
}
