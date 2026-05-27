package com.aowen.predcompanion.core.testing.fakes.data.network

import com.aowen.predcompanion.core.network.model.NetworkHero
import com.aowen.predcompanion.core.network.model.NetworkHeroAbility

val fakeNetworkHero = NetworkHero(
    id = 123,
    name = "test",
    displayName = "Test",
    stats = listOf(3, 4, 5, 6),
    classes = listOf("Assassin", "Fighter"),
    roles = listOf("Offlane", "Jungle"),
    abilities = listOf(
        NetworkHeroAbility(
            displayName = "ability",
            image = "test",
            gameDescription = "gameDescription",
            menuDescription = "menuDescription",
            cooldown = listOf(1.2f, 3.4f, 5.6f),
            cost = listOf(1f, 2f, 3f)

        )
    ),
    baseStats = fakeNetworkHeroBaseStats,
)

val fakeNetworkHero2 = NetworkHero(
    id = 123,
    name = "test",
    displayName = "Test",
    stats = listOf(3, 4, 5, 6),
    classes = listOf("class1", "class2"),
    roles = listOf("Carry", "role2"),
    abilities = listOf(
        NetworkHeroAbility(
            displayName = "ability",
            image = "test",
            gameDescription = "gameDescription",
            menuDescription = "menuDescription",
            cooldown = listOf(1.2f, 3.4f, 5.6f),
            cost = listOf(1f, 2f, 3f)

        )
    ),
    baseStats = fakeNetworkHeroBaseStats,
)