package com.aowen.monolith.data

import com.aowen.monolith.network.RetrofitHelper
import com.aowen.monolith.network.utils.trimExtraNewLine

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
        image = RetrofitHelper.getHeroAbilityImageUrl(this.image),
        gameDescription = gameDescription,
        menuDescription = menuDescription?.trimExtraNewLine(),
        cooldown = cooldown,
        cost = cost,
    )

data class HeroDetails(
    val id: Int = 0,
    val name: String = "",
    val displayName: String = "",
    val imageId: Int? = null,
    val stats: List<Int> = emptyList(),
    val classes: List<HeroClass?> = emptyList(),
    val roles: List<HeroRole?> = emptyList(),
    val abilities: List<AbilityDetails> = emptyList(),
    val baseStats: HeroBaseStats = HeroBaseStats()
)

fun HeroDto.create(): HeroDetails =
    HeroDetails(
        id = id,
        name = name,
        displayName = displayName,
        stats = stats,
        classes = classes.toHeroClass(),
        roles = roles.toHeroRole(),
        imageId = Hero.values().firstOrNull { it.heroName == displayName }?.drawableId,
        abilities = abilities.map {
            it.create()
        },
        baseStats = baseStats.create()
    )

enum class HeroRole {
    Midlane,
    Offlane,
    Jungle,
    Carry,
    Support,
}

enum class HeroClass {
    Assassin,
    Fighter,
    Mage,
    Sharpshooter,
    Support,
    Tank
}

fun List<String>.toHeroRole(): List<HeroRole?> =
    map {
        HeroRole.values().firstOrNull { role -> role.name == it }
    }

fun List<String>.toHeroClass(): List<HeroClass?> =
    map {
        HeroClass.values().firstOrNull { heroClass -> heroClass.name == it }
    }