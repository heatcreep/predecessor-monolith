package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.BaseStatsDto

val fakeBaseStatsDto = BaseStatsDto(
    maxHealth = listOf(100.0f, 200.0f, 300.0f),
    healthRegen = listOf(5.0f, 10.0f, 15.0f),
    maxMana = listOf(50.0f, 100.0f, 150.0f),
    manaRegen = listOf(2.5f, 5.0f, 7.5f),
    attackSpeed = listOf(1.0f, 1.5f, 2.0f),
    physicalArmor = listOf(10.0f, 20.0f, 30.0f),
    magicalArmor = listOf(10.0f, 20.0f, 30.0f),
    physicalPower = listOf(10.0f, 20.0f, 30.0f),
    movementSpeed = listOf(5.0f, 10.0f, 15.0f),
    cleave = listOf(10.0f, 20.0f, 30.0f),
    attackRange = listOf(1.0f, 1.5f, 2.0f)
)