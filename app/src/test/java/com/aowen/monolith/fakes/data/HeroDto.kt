package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.AbilityDto
import com.aowen.monolith.data.HeroDto

val fakeHeroDto = HeroDto(
    id = 123,
    name = "test",
    displayName = "Test",
    stats = listOf(3, 4, 5, 6),
    classes = listOf("class1", "class2"),
    roles = listOf("role1", "role2"),
    abilities = listOf(
        AbilityDto(
            displayName = "ability",
            image = "test",
            gameDescription = "gameDescription",
            menuDescription = "menuDescription",
            cooldown = listOf(1.2f, 3.4f, 5.6f),
            cost = listOf(1, 2, 3)

        )
    ),
    baseStats = fakeBaseStatsDto,
)