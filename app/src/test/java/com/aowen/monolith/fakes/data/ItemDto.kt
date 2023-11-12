package com.aowen.monolith.fakes.data

import com.aowen.monolith.data.EffectDto
import com.aowen.monolith.data.ItemDto

val effectDto1 = EffectDto(
    name = "Effect1",
    active = true,
    gameDescription = "Effect 1 Game Description",
    menuDescription = "Effect 1 Menu Description"
)

val effectDto2 = EffectDto(
    name = "Effect2",
    active = false,
    gameDescription = "Effect 2 Game Description",
    menuDescription = "Effect 2 Menu Description"
)

val fakeItemDto = ItemDto(
    id = 1,
    gameId = 101,
    name = "Item B",
    displayName = "Item B",
    image = "http://example.com/item1.png",
    price = 100,
    totalPrice = 200,
    slotType = "Weapon",
    rarity = "Rare",
    aggressionType = "Offensive",
    heroClass = "Warrior",
    requiredLevel = 10,
    stats = mapOf("max_health" to 10.0, "max_mana" to 5.0),
    effects = listOf(effectDto1, effectDto2),
    requirements = listOf("Item2", "Item3"),
    buildPath = listOf("Item4", "Item5")
)

val fakeItemDto2 = ItemDto(
    id = 2,
    gameId = 102,
    name = "Item A",
    displayName = "Item A",
    image = "http://example.com/item1.png",
    price = 100,
    totalPrice = 200,
    slotType = "Weapon",
    rarity = "Legendary",
    aggressionType = "Offensive",
    heroClass = "Warrior",
    requiredLevel = 10,
    stats = mapOf("max_health" to 15.0, "max_mana" to 5.0, "lifesteal" to 9.0),
    effects = listOf(effectDto1, effectDto2),
    requirements = listOf("Item2", "Item3"),
    buildPath = listOf("Item4", "Item5")
)

val fakeItemDto3 = ItemDto(
    id = 3,
    gameId = 103,
    name = "Item C",
    displayName = "Item C",
    image = "http://example.com/item1.png",
    price = 100,
    totalPrice = 200,
    slotType = "Weapon",
    rarity = "Legendary",
    aggressionType = "Offensive",
    heroClass = "Warrior",
    requiredLevel = 10,
    stats = mapOf("max_health" to 15.0, "max_mana" to 5.0, "lifesteal" to 9.0),
    effects = listOf(effectDto1, effectDto2),
    requirements = listOf("Item2", "Item3"),
    buildPath = listOf("Item4", "Item5")
)

val fakeItemDto4 = ItemDto(
    id = 4,
    gameId = 104,
    name = "Item D",
    displayName = "Item D",
    image = "http://example.com/item1.png",
    price = 100,
    totalPrice = 200,
    slotType = "Weapon",
    rarity = "Legendary",
    aggressionType = "Offensive",
    heroClass = "Warrior",
    requiredLevel = 10,
    stats = mapOf("max_health" to 15.0, "max_mana" to 5.0),
    effects = listOf(effectDto1, effectDto2),
    requirements = listOf("Item2", "Item3"),
    buildPath = listOf("Item4", "Item5")
)