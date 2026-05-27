package com.aowen.predcompanion.core.model.data

import java.math.BigDecimal

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
