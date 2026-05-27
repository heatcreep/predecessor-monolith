package com.aowen.predcompanion.core.testing.fakes.data.network

import com.aowen.predcompanion.core.network.model.NetworkItem
import com.aowen.predcompanion.core.network.model.NetworkItemEffect


val fakeNetworkItemEffect = NetworkItemEffect(
    name = "Effect1",
    active = true,
    cooldown = "",
    menuDescription = "Effect 1 Menu Description"
)

val fakeNetworkItemEffect2 = NetworkItemEffect(
    name = "Effect2",
    active = false,
    cooldown = "",
    menuDescription = "Effect 2 Menu Description"
)

val fakeNetworkItem = NetworkItem(
    id = 1,
    gameId = 101,
    name = "Item B",
    displayName = "Item B",
    image = "item1.png",
    price = 100,
    totalPrice = 200,
    slotType = "Weapon",
    rarity = "Rare",
    aggressionType = "Offensive",
    heroClass = "Warrior",
    requiredLevel = 10,
    stats = mapOf("max_health" to 10.0, "max_mana" to 5.0),
    effects = listOf(fakeNetworkItemEffect, fakeNetworkItemEffect2),
    requirements = listOf("Item2", "Item3"),
    buildPath = listOf("Item4", "Item5")
)

val fakeNetworkItem2 = NetworkItem(
    id = 2,
    gameId = 102,
    name = "Item A",
    displayName = "Item A",
    image = "item2.png",
    price = 100,
    totalPrice = 200,
    slotType = "Weapon",
    rarity = "Legendary",
    aggressionType = "Offensive",
    heroClass = "Warrior",
    requiredLevel = 10,
    stats = mapOf("max_health" to 15.0, "max_mana" to 5.0, "lifesteal" to 9.0),
    effects = listOf(fakeNetworkItemEffect, fakeNetworkItemEffect2),
    requirements = listOf("Item2", "Item3"),
    buildPath = listOf("Item4", "Item5")
)

val fakeNetworkItem3 = NetworkItem(
    id = 3,
    gameId = 103,
    name = "Item C",
    displayName = "Item C",
    image = "item3.png",
    price = 100,
    totalPrice = 200,
    slotType = "Weapon",
    rarity = "Legendary",
    aggressionType = "Offensive",
    heroClass = "Warrior",
    requiredLevel = 10,
    stats = mapOf("max_health" to 15.0, "max_mana" to 5.0, "lifesteal" to 9.0),
    effects = listOf(fakeNetworkItemEffect, fakeNetworkItemEffect2),
    requirements = listOf("Item2", "Item3"),
    buildPath = listOf("Item4", "Item5")
)

val fakeNetworkItem4 = NetworkItem(
    id = 4,
    gameId = 104,
    name = "Item D",
    displayName = "Item D",
    image = "item4.png",
    price = 100,
    totalPrice = 200,
    slotType = "Weapon",
    rarity = "Legendary",
    aggressionType = "Offensive",
    heroClass = "Warrior",
    requiredLevel = 10,
    stats = mapOf("max_health" to 15.0, "max_mana" to 5.0),
    effects = listOf(fakeNetworkItemEffect, fakeNetworkItemEffect2),
    requirements = listOf("Item2", "Item3"),
    buildPath = listOf("Item4", "Item5")
)