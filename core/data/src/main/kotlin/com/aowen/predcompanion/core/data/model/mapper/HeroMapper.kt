package com.aowen.predcompanion.core.data.model.mapper

import com.aowen.predcompanion.core.model.data.AbilityDetails
import com.aowen.predcompanion.core.model.data.FavoriteHero
import com.aowen.predcompanion.core.model.data.Hero
import com.aowen.predcompanion.core.model.data.HeroClass
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.network.model.NetworkFavoriteHero
import com.aowen.predcompanion.core.network.model.NetworkHero
import javax.inject.Inject
import com.aowen.predcompanion.core.resources.R as coreResources

class HeroMapper @Inject constructor() {

    val heroMap = Hero.entries.associateBy { it.heroId }
    val heroClassMap = HeroClass.entries.associateBy { it.label }
    val heroRoleMap = HeroRole.entries.associateBy { it.roleName }

    fun getHeroName(heroId: Long) = heroMap[heroId]?.heroName ?: "Hero"

    fun getHeroRole(roleName: String? = ""): HeroRole? = heroRoleMap[roleName?.lowercase()]

    fun getHeroClass(className: String? = ""): HeroClass? = heroClassMap[className?.lowercase()]

    fun getHeroImage(heroId: Long) = heroMap[heroId]?.drawableId ?: coreResources.drawable.unknown

    fun getHeroRolesFrom(roleNames: List<String>): List<HeroRole> =
        roleNames.mapNotNull { getHeroRole(it) }

    fun getHeroClassesFrom(classLabels: List<String>): List<HeroClass> =
        classLabels.mapNotNull { getHeroClass(it) }

    fun getHeroDetailsFrom(networkHero: NetworkHero): HeroDetails {
        val reorderedAbilities = if (networkHero.abilities.isNotEmpty()) {
            val lastElement = networkHero.abilities.last()
            networkHero.abilities.dropLast(1).toMutableList().apply {
                add(0, lastElement)
            }
        } else networkHero.abilities

        val hero = Hero.entries.firstOrNull { it.heroName == networkHero.displayName }
        return HeroDetails(
            id = networkHero.id,
            name = networkHero.name,
            displayName = networkHero.displayName,
            stats = networkHero.stats,
            classes = getHeroClassesFrom(networkHero.classes),
            roles = getHeroRolesFrom(networkHero.roles),
            imageUrl = "https://pred.gg${networkHero.image}",
            posterImageId = hero?.posterDrawableId,
            abilities = reorderedAbilities.map {
                AbilityDetails(
                    displayName = it.displayName,
                    image = it.image,
                    gameDescription = it.gameDescription,
                    menuDescription = it.menuDescription,
                    cooldown = it.cooldown,
                    cost = it.cost
                )
            },
            baseStats = HeroDetails.HeroBaseStats(
                maxHealth = networkHero.baseStats.maxHealth.map { it.toBigDecimal() },
                healthRegen = networkHero.baseStats.healthRegen.map { it.toBigDecimal() },
                maxMana = networkHero.baseStats.maxMana?.map { it.toBigDecimal() } ?: emptyList(),
                manaRegen = networkHero.baseStats.manaRegen?.map { it.toBigDecimal() }
                    ?: emptyList(),
                attackSpeed = networkHero.baseStats.attackSpeed.map { it.toBigDecimal() },
                physicalArmor = networkHero.baseStats.physicalArmor.map { it.toBigDecimal() },
                magicalArmor = networkHero.baseStats.magicalArmor.map { it.toBigDecimal() },
                physicalPower = networkHero.baseStats.physicalPower.map { it.toBigDecimal() },
                movementSpeed = networkHero.baseStats.movementSpeed.first(),
                cleave = networkHero.baseStats.cleave.first(),
                attackRange = networkHero.baseStats.attackRange.first(),
            )
        )
    }

    fun getFavoriteHeroFrom(networkFavoriteHero: NetworkFavoriteHero): FavoriteHero =
        FavoriteHero(
            id = networkFavoriteHero.id.toInt(),
            gameId = networkFavoriteHero.gameId ?: 0,
            name = networkFavoriteHero.name,
            displayName = networkFavoriteHero.displayName,
            stats = networkFavoriteHero.stats,
            classes = getHeroClassesFrom(networkFavoriteHero.classes),
            roles = getHeroRolesFrom(networkFavoriteHero.roles),
            visible = networkFavoriteHero.visible,
            enabled = networkFavoriteHero.enabled,
            createdAt = networkFavoriteHero.createdAt,
            updatedAt = networkFavoriteHero.updatedAt,
        )

}