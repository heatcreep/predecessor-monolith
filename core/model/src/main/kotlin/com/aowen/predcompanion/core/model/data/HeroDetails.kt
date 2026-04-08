package com.aowen.predcompanion.core.model.data

import com.aowen.predcompanion.core.common.network.RetrofitHelper
import com.aowen.predcompanion.core.common.network.utils.trimExtraNewLine
import com.aowen.predcompanion.data.AbilityDto
import com.aowen.predcompanion.data.FavoriteHeroDto
import com.aowen.predcompanion.data.HeroBaseStats
import com.aowen.predcompanion.data.HeroDto
import com.aowen.predcompanion.data.create
import com.aowen.predcompanion.core.resources.R as coreResources

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
    val id: Long = 0,
    val name: String = "",
    val displayName: String = "",
    val imageId: Int? = null,
    val posterImageId: Int? = null,
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

fun HeroDto.asHeroDetails(): HeroDetails {
    val reorderedAbilities = if (abilities.isNotEmpty()) {
        val lastElement = abilities.last()
        abilities.dropLast(1).toMutableList().apply {
            add(0, lastElement)
        }
    } else abilities

    val hero = Hero.entries.firstOrNull { it.heroName == displayName }
    return HeroDetails(
        id = id,
        name = name,
        displayName = displayName,
        stats = stats,
        classes = classes.toHeroClass().filterNotNull(),
        roles = roles.toHeroRole().filterNotNull(),
        imageId = hero?.drawableId,
        posterImageId = hero?.posterDrawableId,
        abilities = reorderedAbilities.map {
            it.create()
        },
        baseStats = baseStats.create()
    )
}

fun FavoriteHeroDto.create(): FavoriteHero =
    FavoriteHero(
        id = id.toInt(),
        gameId = gameId ?: 0,
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

enum class HeroClass(val label: String, val iconRes: Int) {
    MAGE(label = "Mage", iconRes = coreResources.drawable.simple_mid),
    SUPPORT(label = "Support", iconRes = coreResources.drawable.simple_support),
    SHARPSHOOTER(label = "Sharpshooter", iconRes = coreResources.drawable.simple_carry),
    TANK(label = "Tank", iconRes = coreResources.drawable.simple_tank),
    FIGHTER(label = "Fighter", iconRes = coreResources.drawable.simple_fighter),
    ASSASSIN(label = "Assassin", iconRes = coreResources.drawable.simple_assassin),
    UNKNOWN(label = "Unknown", iconRes = coreResources.drawable.unknown)
}

fun List<String>.toHeroRole(): List<HeroRole?> =
    map {
        HeroRole.entries.firstOrNull { role -> role.name == it }
    }

fun List<String>.toHeroClass(): List<HeroClass?> =
    map {
        HeroClass.entries.firstOrNull { heroClass -> heroClass.label == it }
    }