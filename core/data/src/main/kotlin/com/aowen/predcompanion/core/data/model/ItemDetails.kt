package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.data.helpers.ImageHelpers
import com.aowen.predcompanion.core.model.data.HeroClass
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.model.data.ItemDetails.StatDetails
import com.aowen.predcompanion.core.model.data.Rarity
import com.aowen.predcompanion.core.model.data.SlotType
import com.aowen.predcompanion.core.network.apollo.fragment.ItemFragment
import com.aowen.predcompanion.core.network.apollo.type.ItemDescriptionStat
import com.aowen.predcompanion.core.network.apollo.type.ItemRarity
import com.aowen.predcompanion.core.network.apollo.type.HeroClass as ApolloHeroClass
import com.aowen.predcompanion.core.network.apollo.type.SlotType as ApolloSlotType
import com.aowen.predcompanion.core.resources.R as coreResources

fun ItemFragment.asItemDetails(): ItemDetails {
    val itemData = this.data ?: return ItemDetails()
    val rarity = when (itemData.rarity) {
        ItemRarity.UNCOMMON -> Rarity.UNCOMMON
        ItemRarity.RARE -> Rarity.RARE
        ItemRarity.EPIC -> Rarity.EPIC
        ItemRarity.LEGENDARY -> Rarity.LEGENDARY
        else -> Rarity.COMMON
    }

    val heroClass = when (itemData.`class`) {
        ApolloHeroClass.FIGHTER -> HeroClass.FIGHTER
        ApolloHeroClass.TANK -> HeroClass.TANK
        ApolloHeroClass.ASSASSIN -> HeroClass.ASSASSIN
        ApolloHeroClass.MAGE -> HeroClass.MAGE
        ApolloHeroClass.SUPPORT -> HeroClass.SUPPORT
        ApolloHeroClass.SHARPSHOOTER -> HeroClass.SHARPSHOOTER
        else -> HeroClass.UNKNOWN
    }

    val slotType = when (itemData.slotType) {
        ApolloSlotType.TRINKET -> SlotType.TRINKET
        ApolloSlotType.CREST -> SlotType.CREST
        ApolloSlotType.ACTIVE -> SlotType.ACTIVE
        else -> SlotType.PASSIVE
    }
    return ItemDetails(
        id = this.id,
        gameId = itemData.gameId.toString(),
        name = itemData.name,
        displayName = itemData.displayName,
        imageSrc = ImageHelpers.buildAssetsUrl(itemData.icon),
        price = itemData.price,
        totalPrice = itemData.totalPrice,
        slotType = slotType,
        rarity = rarity,
        aggressionType = null,
        heroClass = heroClass,
        stats = itemData.stats.map { it.createStatDetails() },
        effects = itemData.effects.map {
            ItemDetails.EffectDetails(
                name = it.name,
                active = it.active,
                condition = it.condition,
                cooldown = it.cooldown,
                menuDescription = it.text,
            )

        }
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

private fun ItemFragment.Stat.createStatDetails(): ItemDetails.StatDetails {
    return when (this.stat) {
        ItemDescriptionStat.ABILITY_HASTE -> StatDetails(
            iconId = coreResources.drawable.ability_haste,
            name = "Ability Haste",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.HEALTH -> StatDetails(
            iconId = coreResources.drawable.max_health,
            name = "Max Health",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.MANA -> StatDetails(
            iconId = coreResources.drawable.max_mana,
            name = "Max Mana",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.BASE_HEALTH_REGENERATION -> StatDetails(
            iconId = coreResources.drawable.health_regen,
            name = "Health Regen",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.BASE_MANA_REGENERATION -> StatDetails(
            iconId = coreResources.drawable.mana_regen,
            name = "Mana Regen",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.PHYSICAL_POWER -> StatDetails(
            iconId = coreResources.drawable.physical_power,
            name = "Physical Power",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.MAGICAL_POWER -> StatDetails(
            iconId = coreResources.drawable.magical_power,
            name = "Magical Power",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.ATTACK_SPEED -> StatDetails(
            iconId = coreResources.drawable.attack_speed,
            name = "Attack Speed",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.PHYSICAL_ARMOR -> StatDetails(
            iconId = coreResources.drawable.physical_armor,
            name = "Physical Armor",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.MAGICAL_ARMOR -> StatDetails(
            iconId = coreResources.drawable.magical_armor,
            name = "Magical Armor",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.PHYSICAL_PENETRATION -> StatDetails(
            iconId = coreResources.drawable.physical_pen,
            name = "Physical Penetration",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.MAGICAL_PENETRATION -> StatDetails(
            iconId = coreResources.drawable.magical_pen,
            name = "Magical Penetration",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.CRITICAL_CHANCE -> StatDetails(
            iconId = coreResources.drawable.critical_chance,
            name = "Critical Chance",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.GOLD_PER_SECOND -> StatDetails(
            iconId = coreResources.drawable.gold_per_second,
            name = "Gold Per Second",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.TENACITY -> StatDetails(
            iconId = coreResources.drawable.tenacity,
            name = "Tenacity",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.MOVEMENT_SPEED -> StatDetails(
            iconId = coreResources.drawable.movement_speed,
            name = "Movement Speed",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.LIFESTEAL -> StatDetails(
            iconId = coreResources.drawable.lifesteal,
            name = "Lifesteal",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.DEFAULT -> StatDetails(
            iconId = coreResources.drawable.unknown,
            name = "Unknown",
            value = this.value.toStatValue()
        )

        ItemDescriptionStat.PERC_PHYSICAL_PENETRATION -> StatDetails(
            iconId = coreResources.drawable.physical_pen,
            name = "% Physical Penetration",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.PERC_MAGICAL_PENETRATION -> StatDetails(
            iconId = coreResources.drawable.magical_pen,
            name = "% Magical Penetration",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.MAGICAL_LIFESTEAL -> StatDetails(
            iconId = coreResources.drawable.magical_lifesteal,
            name = "Magical Lifesteal",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.OMNIVAMP -> StatDetails(
            iconId = coreResources.drawable.omnivamp,
            name = "Omnivamp",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.HEAL_AND_SHIELD_POWER -> StatDetails(
            iconId = coreResources.drawable.heal_shield_increase,
            name = "Heal and Shield Increase",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.UNKNOWN__ -> StatDetails(
            iconId = coreResources.drawable.unknown,
            name = "Unknown",
            value = this.value.toStatValue()
        )
    }
}

private fun Map<String, Double>?.createStatDetails(): List<ItemDetails.StatDetails> {
    val listOfStats = mutableListOf<ItemDetails.StatDetails>()
    for ((key, value) in this ?: emptyMap()) {
        val statDetails = when (key.lowercase()) {
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