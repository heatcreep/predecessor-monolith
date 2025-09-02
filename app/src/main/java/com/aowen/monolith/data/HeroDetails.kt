package com.aowen.monolith.data

import com.aowen.monolith.R
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

    val hero = Hero.entries.firstOrNull() { it.heroName == displayName }
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
    MAGE(label = "Mage", iconRes = R.drawable.simple_mid),
    SUPPORT(label = "Support", iconRes = R.drawable.simple_support),
    SHARPSHOOTER(label = "Sharpshooter", iconRes = R.drawable.simple_carry),
    TANK(label = "Tank", iconRes = R.drawable.simple_tank),
    FIGHTER(label = "Fighter", iconRes = R.drawable.simple_fighter),
    ASSASSIN(label = "Assassin", iconRes = R.drawable.simple_assassin),
    UNKNOWN(label = "Unknown", iconRes = R.drawable.unknown)
}

fun List<String>.toHeroRole(): List<HeroRole?> =
    map {
        HeroRole.entries.firstOrNull { role -> role.name == it }
    }

fun List<String>.toHeroClass(): List<HeroClass?> =
    map {
        HeroClass.entries.firstOrNull { heroClass -> heroClass.label == it }
    }