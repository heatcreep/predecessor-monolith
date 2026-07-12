package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.database.model.AbilityDetailsEntity
import com.aowen.predcompanion.core.database.model.HeroBaseStatsEntity
import com.aowen.predcompanion.core.database.model.HeroDetailsEntity
import com.aowen.predcompanion.core.model.data.AbilityDetails
import com.aowen.predcompanion.core.model.data.HeroClass
import com.aowen.predcompanion.core.model.data.HeroDetails
import com.aowen.predcompanion.core.model.data.HeroRole
import com.aowen.predcompanion.core.network.apollo.fragment.HeroFragment
import com.aowen.predcompanion.core.network.apollo.type.BaseAttribute

fun HeroFragment.asHeroDetails(): HeroDetails {
    val heroData = this.data?: return HeroDetails(
        id = 0L,
        name = "",
    )
    val abilities = heroData.abilities
    val reorderedAbilities = if (abilities.isNotEmpty()) {
        val lastElement = abilities.last()
        abilities.dropLast(1).toMutableList().apply { add(0, lastElement) }
    } else abilities

    val attributesByType = heroData.attributes.associateBy { it.stat }

    fun attributeValues(stat: BaseAttribute) =
        attributesByType[stat]?.values?.map { it.toBigDecimal() } ?: emptyList()

    fun attributeFirst(stat: BaseAttribute) =
        attributesByType[stat]?.values?.firstOrNull()?.toFloat() ?: 0f

    return HeroDetails(
        id = id.toLong(),
        name = name,
        displayName = heroData.displayName,
        stats = listOf(
            heroData.mainAttributes.attackPower,
            heroData.mainAttributes.abilityPower,
            heroData.mainAttributes.durability,
            heroData.mainAttributes.mobility,
        ),
        classes = heroData.classes.mapNotNull { heroClass ->
            getHeroClass(heroClass.name.lowercase())
        },
        roles = heroData.roles.mapNotNull { heroRole ->
            getHeroRole(heroRole.name.lowercase())
        },
        imageUrl = buildAssetsUrl(heroData.icon),
        posterImageId = heroData.defaultSkin?.portrait?.let { imageId ->
            buildAssetsUrl(imageId)
        },
        abilities = reorderedAbilities.map {
            AbilityDetails(
                displayName = it.displayName,
                image = it.icon,
                gameDescription = it.gameDescription,
                menuDescription = it.menuDescription,
                cooldown = it.cooldown.map { cd -> cd.toFloat() },
                cost = it.cost.map { c -> c.toFloat() },
            )
        },
        baseStats = HeroDetails.HeroBaseStats(
            maxHealth = attributeValues(BaseAttribute.MAX_HEALTH),
            healthRegen = attributeValues(BaseAttribute.HEALTH_REGEN),
            maxMana = attributeValues(BaseAttribute.MAX_MANA),
            manaRegen = attributeValues(BaseAttribute.MANA_REGEN),
            attackSpeed = attributeValues(BaseAttribute.ATTACK_SPEED),
            physicalArmor = attributeValues(BaseAttribute.BASIC_ARMOR),
            magicalArmor = attributeValues(BaseAttribute.ABILITY_ARMOR),
            physicalPower = attributeValues(BaseAttribute.DAMAGE),
            movementSpeed = attributeFirst(BaseAttribute.MOVEMENT_SPEED),
            cleave = attributeFirst(BaseAttribute.CLEAVE),
            attackRange = attributeFirst(BaseAttribute.ATTACK_RANGE),
        ),
    )
}

fun HeroFragment.asEntity(): HeroDetailsEntity? {
    val heroData = this.data ?: return null
    val attributesByType = heroData.attributes.associateBy { it.stat }

    fun attributeValues(stat: BaseAttribute) =
        attributesByType[stat]?.values?.map { it.toBigDecimal() } ?: emptyList()

    fun attributeFirst(stat: BaseAttribute) =
        attributesByType[stat]?.values?.firstOrNull()?.toFloat() ?: 0f
    return HeroDetailsEntity(
        id = id,
        name = name,
        displayName = heroData.displayName,
        imageUrl = buildAssetsUrl(heroData.icon),
        posterImageId = heroData.defaultSkin?.portrait?.let { buildAssetsUrl(it) },
        stats = this.data?.mainAttributes?.let {
            listOf(
                it.attackPower,
                it.abilityPower,
                it.durability,
                it.mobility,
            )
        } ?: emptyList(),
        classes = heroData.classes.map { it.name.lowercase() },
        roles = heroData.roles.map { it.name.lowercase() },
        abilities = heroData.abilities.map {
            AbilityDetailsEntity(
                displayName = it.displayName,
                image = it.icon,
                gameDescription = it.gameDescription,
                menuDescription = it.menuDescription,
                cooldown = it.cooldown.map { cd -> cd.toFloat() },
                cost = it.cost.map { c -> c.toFloat() },
            )
        },
        baseStats = HeroBaseStatsEntity(
            maxHealth = attributeValues(BaseAttribute.MAX_HEALTH),
            healthRegen = attributeValues(BaseAttribute.HEALTH_REGEN),
            maxMana = attributeValues(BaseAttribute.MAX_MANA),
            manaRegen = attributeValues(BaseAttribute.MANA_REGEN),
            attackSpeed = attributeValues(BaseAttribute.ATTACK_SPEED),
            physicalArmor = attributeValues(BaseAttribute.BASIC_ARMOR),
            magicalArmor = attributeValues(BaseAttribute.ABILITY_ARMOR),
            physicalPower = attributeValues(BaseAttribute.DAMAGE),
            movementSpeed = attributeFirst(BaseAttribute.MOVEMENT_SPEED),
            cleave = attributeFirst(BaseAttribute.CLEAVE),
            attackRange = attributeFirst(BaseAttribute.ATTACK_RANGE),
        ),
    )
}


fun HeroDetails.asEntity() = HeroDetailsEntity(
    id = id.toString(),
    name = name,
    displayName = displayName,
    imageUrl = imageUrl,
    posterImageId = posterImageId,
    stats = stats,
    classes = classes.map { it.label },
    roles = roles.map { it.roleName },
    abilities = abilities.map {
        AbilityDetailsEntity(
            displayName = it.displayName,
            image = it.image,
            gameDescription = it.gameDescription,
            menuDescription = it.menuDescription,
            cooldown = it.cooldown,
            cost = it.cost,
        )
    },
    baseStats = HeroBaseStatsEntity(
        maxHealth = baseStats.maxHealth,
        healthRegen = baseStats.healthRegen,
        maxMana = baseStats.maxMana,
        manaRegen = baseStats.manaRegen,
        attackSpeed = baseStats.attackSpeed,
        physicalArmor = baseStats.physicalArmor,
        magicalArmor = baseStats.magicalArmor,
        physicalPower = baseStats.physicalPower,
        movementSpeed = baseStats.movementSpeed,
        cleave = baseStats.cleave,
        attackRange = baseStats.attackRange,
    ),
)

fun HeroDetailsEntity.asHeroDetails() = HeroDetails(
    id = id.toLong(),
    name = name,
    displayName = displayName,
    imageUrl = imageUrl,
    posterImageId = posterImageId,
    stats = stats,
    classes = classes.mapNotNull { getHeroClass(it) },
    roles = roles.mapNotNull { getHeroRole(it) },
    abilities = abilities.map {
        AbilityDetails(
            displayName = it.displayName,
            image = it.image,
            gameDescription = it.gameDescription,
            menuDescription = it.menuDescription,
            cooldown = it.cooldown,
            cost = it.cost,
        )
    },
    baseStats = HeroDetails.HeroBaseStats(
        maxHealth = baseStats.maxHealth,
        healthRegen = baseStats.healthRegen,
        maxMana = baseStats.maxMana,
        manaRegen = baseStats.manaRegen,
        attackSpeed = baseStats.attackSpeed,
        physicalArmor = baseStats.physicalArmor,
        magicalArmor = baseStats.magicalArmor,
        physicalPower = baseStats.physicalPower,
        movementSpeed = baseStats.movementSpeed,
        cleave = baseStats.cleave,
        attackRange = baseStats.attackRange,
    ),
)

private fun buildAssetsUrl(imageId: String) = "https://pred.gg/assets/${imageId}.webp"

private fun getHeroClass(className: String?): HeroClass? = HeroClass.entries.associateBy { it.label }[className?.lowercase()]

private fun getHeroRole(roleName: String?): HeroRole? = HeroRole.entries.associateBy { it.roleName }[roleName?.lowercase()]