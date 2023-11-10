package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.HeroBaseStats

val defaultHeroBaseStats = HeroBaseStats(
    maxHealth = listOf(
        100.0f.toBigDecimal(),
        200.0f.toBigDecimal(),
        300.0f.toBigDecimal()
    ),
    healthRegen = listOf(
        5.0f.toBigDecimal(),
        10.0f.toBigDecimal(),
        15.0f.toBigDecimal()
    ),
    maxMana = listOf(
        50.0f.toBigDecimal(),
        100.0f.toBigDecimal(),
        150.0f.toBigDecimal()
    ),
    manaRegen = listOf(
        2.5f.toBigDecimal(),
        5.0f.toBigDecimal(),
        7.5f.toBigDecimal()
    ),
    attackSpeed = listOf(
        1.0f.toBigDecimal(),
        1.5f.toBigDecimal(),
        2.0f.toBigDecimal()
    ),
    physicalArmor = listOf(
        10.0f.toBigDecimal(),
        20.0f.toBigDecimal(),
        30.0f.toBigDecimal()
    ),
    magicalArmor = listOf(
        10.0f.toBigDecimal(),
        20.0f.toBigDecimal(),
        30.0f.toBigDecimal()
    ),
    physicalPower = listOf(
        10.0f.toBigDecimal(),
        20.0f.toBigDecimal(),
        30.0f.toBigDecimal()
    ),
    movementSpeed = 5.0f,
    cleave = 10.0f,
    attackRange = 1.0f
)