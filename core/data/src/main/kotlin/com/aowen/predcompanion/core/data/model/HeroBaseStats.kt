package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.model.data.HeroBaseStats
import com.aowen.predcompanion.core.network.model.NetworkHeroBaseStats

fun NetworkHeroBaseStats.asPlayerDetails(): HeroBaseStats =
    HeroBaseStats(
        maxHealth = maxHealth.map { it.toBigDecimal() },
        healthRegen = healthRegen.map { it.toBigDecimal() },
        maxMana = maxMana?.map { it.toBigDecimal() } ?: emptyList(),
        manaRegen = manaRegen?.map { it.toBigDecimal() } ?: emptyList(),
        attackSpeed = attackSpeed.map { it.toBigDecimal() },
        physicalArmor = physicalArmor.map { it.toBigDecimal() },
        magicalArmor = magicalArmor.map { it.toBigDecimal() },
        physicalPower = physicalPower.map { it.toBigDecimal() },
        movementSpeed = movementSpeed.first(),
        cleave = cleave.first(),
        attackRange = attackRange.first(),
    )