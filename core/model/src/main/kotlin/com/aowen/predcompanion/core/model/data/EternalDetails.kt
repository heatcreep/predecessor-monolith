package com.aowen.predcompanion.core.model.data

data class EternalDetails(
    val id: String,
    val displayName: String,
    val description: String,
    val icon: String,
    val eternalCategory: EternalCategory,
    val firstMinorBlessing: List<BlessingDetails> = emptyList(),
    val secondMinorBlessing: List<BlessingDetails> = emptyList()
)

data class EternalCategory(
    val id: String,
    val colorString: String,
    val categoryDescription: String,
    val categoryType: CategoryType,
    val displayName: String,
) {
    enum class CategoryType {
        ANOMALIES,
        DIVINES,
        DREADNOUGHTS,
        HARBINGERS,
        PRIMARCHS,
        SOVEREIGNS,
        UNKNOWN
    }
}

data class BlessingDetails(
    val id: String,
    val displayName: String,
    val description: String,
    val icon: String,
)
