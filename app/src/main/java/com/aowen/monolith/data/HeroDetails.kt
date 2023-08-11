package com.aowen.monolith.data

data class AbilityDetails(
    val displayName: String,
    val image: String,
    val gameDescription: String,
    val menuDescription: String? = null,
    val cooldown: List<Float?>,
    val cost: List<Int>,
)

fun AbilityDto.create(): AbilityDetails =
    AbilityDetails(
        displayName = displayName,
        image = image,
        gameDescription = gameDescription,
        menuDescription = menuDescription,
        cooldown = cooldown,
        cost = cost,
    )

data class HeroDetails(
    val id: String = "",
    val name: String = "",
    val displayName: String = "",
    val imageId: Int? = null,
    val stats: List<Int> = emptyList(),
    val classes: List<String> = emptyList(),
    val roles: List<String> = emptyList(),
    val abilities: List<AbilityDetails> = emptyList(),
)

fun HeroDto.create(): HeroDetails =
    HeroDetails(
        id = id,
        name = name,
        displayName = displayName,
        stats = stats,
        classes = classes,
        roles = roles,
        imageId = HeroImage.values().firstOrNull { it.heroName == displayName }?.drawableId,
        abilities = abilities.map {
            it.create()
        },
    )