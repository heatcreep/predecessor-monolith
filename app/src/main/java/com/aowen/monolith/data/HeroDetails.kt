package com.aowen.monolith.data

import com.aowen.monolith.network.RetrofitHelper
import com.aowen.monolith.network.utils.trimExtraNewLine

data class AbilityDetails(
    val displayName: String,
    val image: String,
    val gameDescription: String,
    val menuDescription: String? = null,
    val cooldown: List<Float?>,
    val cost: List<Float>,
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
    val classes: List<HeroClass> = emptyList(),
    val roles: List<HeroRole> = emptyList(),
    val abilities: List<AbilityDetails> = emptyList(),
    val baseStats: HeroBaseStats = HeroBaseStats()
)

data class FavoriteHero(
    val id: Int,
    val gameId: Int,
    val name: String,
    val displayName: String,
    val stats: List<Int>,
    val classes: List<HeroClass>,
    val roles: List<HeroRole>,
    val visible: Boolean,
    val enabled: Boolean,
    val createdAt: String,
    val updatedAt: String,
)

fun HeroDto.create(): HeroDetails =
    HeroDetails(
        id = id,
        name = name,
        displayName = displayName,
        stats = stats,
        classes = classes.toHeroClass().filterNotNull(),
        roles = roles.toHeroRole().filterNotNull(),
        imageId = Hero.entries.firstOrNull { it.heroName == displayName }?.drawableId,
        abilities = abilities.map {
            it.create()
        },
        baseStats = baseStats.create()
    )

fun FavoriteHeroDto.create(): FavoriteHero =
    FavoriteHero(
        id = id,
        gameId = gameId,
        name = name,
        displayName = displayName,
        stats = stats,
        classes = classes.toHeroClass().filterNotNull(),
        roles = roles.toHeroRole().filterNotNull(),
        visible = visible,
        enabled = enabled,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

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
        HeroRole.entries.firstOrNull { role -> role.name == it }
    }

fun List<String>.toHeroClass(): List<HeroClass?> =
    map {
        HeroClass.entries.firstOrNull { heroClass -> heroClass.name == it }
    }