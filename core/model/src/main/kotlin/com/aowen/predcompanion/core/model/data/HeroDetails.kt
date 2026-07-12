package com.aowen.predcompanion.core.model.data

import java.math.BigDecimal

data class AbilityDetails(
    val displayName: String,
    val image: String,
    val gameDescription: String,
    val menuDescription: String? = null,
    val cooldown: List<Float?>,
    val cost: List<Float>,
)

data class HeroDetails(
    val id: Long = 0,
    val name: String = "",
    val displayName: String = "",
    val imageUrl: String? = null,
    val posterImageId: String? = null,
    val stats: List<Int> = emptyList(),
    val classes: List<HeroClass> = emptyList(),
    val roles: List<HeroRole> = emptyList(),
    val abilities: List<AbilityDetails> = emptyList(),
    val baseStats: HeroBaseStats = HeroBaseStats()
) {
    data class HeroBaseStats(
        val maxHealth: List<BigDecimal> = emptyList(),
        val healthRegen: List<BigDecimal> = emptyList(),
        val maxMana: List<BigDecimal> = emptyList(),
        val manaRegen: List<BigDecimal> = emptyList(),
        val attackSpeed: List<BigDecimal> = emptyList(),
        val physicalArmor: List<BigDecimal> = emptyList(),
        val magicalArmor: List<BigDecimal> = emptyList(),
        val physicalPower: List<BigDecimal> = emptyList(),
        val movementSpeed: Float = 0f,
        val cleave: Float = 0f,
        val attackRange: Float = 0f,
    )
}

data class FavoriteHero(
    val id: Int,
    val gameId: Int,
    val name: String,
    val displayName: String,
    val stats: List<Int>,
    val classes: List<HeroClass>,
    val roles: List<HeroRole>,
    val visible: Boolean,
    val enabled: Boolean,
    val createdAt: String,
    val updatedAt: String,
)