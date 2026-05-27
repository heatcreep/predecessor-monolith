package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.common.network.RetrofitHelper
import com.aowen.predcompanion.core.model.data.HeroClass
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.model.data.Rarity
import com.aowen.predcompanion.core.model.data.SlotType
import com.aowen.predcompanion.core.network.model.NetworkItem
import com.aowen.predcompanion.core.resources.R as coreResources

fun NetworkItem.asItemDetails(): ItemDetails {

    val rarity = when (this.rarity) {
        "Common" -> Rarity.COMMON
        "Uncommon" -> Rarity.UNCOMMON
        "Rare" -> Rarity.RARE
        "Epic" -> Rarity.EPIC
        "Legendary" -> Rarity.LEGENDARY
        else -> Rarity.COMMON
    }

    val heroClass = when (this.heroClass) {
        "Fighter" -> HeroClass.FIGHTER
        "Tank" -> HeroClass.TANK
        "Assassin" -> HeroClass.ASSASSIN
        "Mage" -> HeroClass.MAGE
        "Support" -> HeroClass.SUPPORT
        "Sharpshooter" -> HeroClass.SHARPSHOOTER
        else -> HeroClass.UNKNOWN
    }

    val slotType = when (this.slotType) {
        "Trinket" -> SlotType.TRINKET
        "Crest" -> SlotType.CREST
        "Active" -> SlotType.ACTIVE
        else -> SlotType.PASSIVE
    }
    return ItemDetails(
        id = this.id,
        gameId = this.gameId ?: 0,
        name = this.name,
        displayName = this.displayName,
        imageSrc = RetrofitHelper.getRankImageUrl(this.image),
        price = this.price ?: 0,
        totalPrice = this.totalPrice,
        slotType = slotType,
        rarity = rarity,
        aggressionType = this.aggressionType,
        heroClass = heroClass,
        requiredLevel = this.requiredLevel,
        stats = this.stats.createStatDetails(),
        effects = this.effects.map {
            ItemDetails.EffectDetails(
                name = it.name,
                active = it.active,
                condition = it.condition,
                cooldown = it.cooldown,
                menuDescription = it.menuDescription,
            )

        },
        requirements = this.requirements,
        buildPath = this.buildPath,
    )
}

private fun Double.toStatValue(): String {
    val isWholeNumber = this == this.toLong().toDouble()
    return if (isWholeNumber) {
        this.toInt().toString()
    } else {
        this.toFloat().toPercentageString()
    }
}

fun Float.toPercentageString(): String {
    return "${(this * 100).toInt()}%"
}

private fun Map<String, Double>?.createStatDetails(): List<ItemDetails.StatDetails> {
    val listOfStats = mutableListOf<ItemDetails.StatDetails>()
    for ((key, value) in this ?: emptyMap()) {
        val statDetails = when (key) {
            "max_health" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.max_health,
                name = "Max Health",
                value = value.toStatValue()
            )

            "max_mana" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.max_mana,
                name = "Max Mana",
                value = value.toStatValue()
            )

            "health_regeneration" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.health_regen,
                name = "Health Regen",
                value = value.toStatValue()
            )

            "mana_regeneration" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.mana_regen,
                name = "Mana Regen",
                value = value.toStatValue()
            )

            "physical_power" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.physical_power,
                name = "Physical Power",
                value = value.toStatValue()
            )

            "magical_power" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.magical_power,
                name = "Magical Power",
                value = value.toStatValue()
            )

            "attack_speed" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.attack_speed,
                name = "Attack Speed",
                value = value.toStatValue()
            )

            "physical_armor" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.physical_armor,
                name = "Physical Armor",
                value = value.toStatValue()
            )

            "magical_armor" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.magical_armor,
                name = "Magical Armor",
                value = value.toStatValue()
            )

            "heal_shield_increase" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.heal_shield_increase,
                name = "Heal and Shield Increase",
                value = value.toStatValue()
            )

            "ability_haste" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.ability_haste,
                name = "Ability Haste",
                value = value.toStatValue()
            )

            "lifesteal" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.lifesteal,
                name = "Lifesteal",
                value = value.toStatValue()
            )

            "magical_lifesteal" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.magical_lifesteal,
                name = "Magical Lifesteal",
                value = value.toStatValue()
            )

            "omnivamp" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.omnivamp,
                name = "Omnivamp",
                value = value.toStatValue()
            )

            "movement_speed" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.movement_speed,
                name = "Movement Speed",
                value = value.toStatValue()
            )

            "physical_penetration" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.physical_pen,
                name = "Physical Penetration",
                value = value.toStatValue()
            )

            "magical_penetration" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.magical_pen,
                name = "Magical Penetration",
                value = value.toStatValue()
            )

            "critical_chance" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.critical_chance,
                name = "Critical Chance",
                value = value.toStatValue()
            )

            "gold_per_second" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.gold_per_second,
                name = "Gold Per Second",
                value = value.toStatValue()
            )

            "tenacity" -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.tenacity,
                name = "Tenacity",
                value = value.toStatValue()
            )

            else -> ItemDetails.StatDetails(
                iconId = coreResources.drawable.unknown,
                name = "Unknown",
                value = value.toStatValue()
            )

        }
        listOfStats.add(statDetails)
    }
    return listOfStats

}