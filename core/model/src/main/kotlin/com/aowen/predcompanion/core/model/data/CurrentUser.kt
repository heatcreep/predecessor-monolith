package com.aowen.predcompanion.core.model.data

data class CurrentUser(
    val id: String,
    val name: String,
    val players: List<Player>,
)

