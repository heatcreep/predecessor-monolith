package com.aowen.monolith.network

abstract class UserState {
    object Loading : UserState()
    data class Unauthenticated(val hasSkippedOnboarding: Boolean) : UserState()
    object Authenticated : UserState()
}
