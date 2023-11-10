package com.aowen.monolith.data

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

fun BaseStatsDto.create(): HeroBaseStats =
    HeroBaseStats(
        maxHealth = maxHealth.map { it.toBigDecimal() },
        healthRegen = healthRegen.map { it.toBigDecimal() },
        maxMana = maxMana.map { it.toBigDecimal() },
        manaRegen = manaRegen.map { it.toBigDecimal() },
        attackSpeed = attackSpeed.map { it.toBigDecimal() },
        physicalArmor = physicalArmor.map { it.toBigDecimal() },
        magicalArmor = magicalArmor.map { it.toBigDecimal() },
        physicalPower = physicalPower.map { it.toBigDecimal() },
        movementSpeed = movementSpeed.first(),
        cleave = cleave.first(),
        attackRange = attackRange.first(),
    )
