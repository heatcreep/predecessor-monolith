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
    name = "Item1",
    displayName = "Item 1",
    image = "http://example.com/item1.png",
    price = 100,
    totalPrice = 200,
    slotType = "Weapon",
    rarity = "Rare",
    aggressionType = "Offensive",
    heroClass = "Warrior",
    requiredLevel = 10,
    stats = mapOf("stat1" to 10.0, "stat2" to 5.0),
    effects = listOf(effectDto1, effectDto2),
    requirements = listOf("Item2", "Item3"),
    buildPath = listOf("Item4", "Item5")
)