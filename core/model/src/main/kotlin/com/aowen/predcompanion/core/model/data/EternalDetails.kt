package com.aowen.predcompanion.core.model.data

data class EternalDetails(
    val id: String,
    val displayName: String,
    val description: String,
    val icon: String,
    val eternalCategory: EternalCategory,
    val blessings: List<BlessingDetails> = emptyList()
)

data class EternalCategory(
    val id: String,
    val displayName: String,
)

data class BlessingDetails(
    val id: String,
    val displayName: String,
    val description: String,
    val icon: String,
)
