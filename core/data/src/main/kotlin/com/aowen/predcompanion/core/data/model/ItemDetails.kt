package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.common.network.RetrofitHelper
import com.aowen.predcompanion.core.data.helpers.ImageHelpers
import com.aowen.predcompanion.core.model.data.HeroClass
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.core.model.data.Rarity
import com.aowen.predcompanion.core.model.data.SlotType
import com.aowen.predcompanion.core.network.apollo.fragment.ItemFragment
import com.aowen.predcompanion.core.network.apollo.type.ItemDescriptionStat
import com.aowen.predcompanion.core.network.apollo.type.ItemRarity
import com.aowen.predcompanion.core.network.model.NetworkItem
import com.aowen.predcompanion.core.network.apollo.type.HeroClass as ApolloHeroClass
import com.aowen.predcompanion.core.network.apollo.type.SlotType as ApolloSlotType
import com.aowen.predcompanion.core.resources.R as coreResources

fun ItemFragment.Data.asItemDetails(): ItemDetails {
    val rarity = when (this.rarity) {
        ItemRarity.UNCOMMON -> Rarity.UNCOMMON
        ItemRarity.RARE -> Rarity.RARE
        ItemRarity.EPIC -> Rarity.EPIC
        ItemRarity.LEGENDARY -> Rarity.LEGENDARY
        else -> Rarity.COMMON
    }

    val heroClass = when (this.`class`) {
        ApolloHeroClass.FIGHTER -> HeroClass.FIGHTER
        ApolloHeroClass.TANK -> HeroClass.TANK
        ApolloHeroClass.ASSASSIN -> HeroClass.ASSASSIN
        ApolloHeroClass.MAGE -> HeroClass.MAGE
        ApolloHeroClass.SUPPORT -> HeroClass.SUPPORT
        ApolloHeroClass.SHARPSHOOTER -> HeroClass.SHARPSHOOTER
        else -> HeroClass.UNKNOWN
    }

    val slotType = when (this.slotType) {
        ApolloSlotType.TRINKET -> SlotType.TRINKET
        ApolloSlotType.CREST -> SlotType.CREST
        ApolloSlotType.ACTIVE -> SlotType.ACTIVE
        else -> SlotType.PASSIVE
    }
    return ItemDetails(
        id = this.id,
        gameId = this.gameId,
        name = this.name,
        displayName = this.displayName,
        imageSrc = ImageHelpers.buildAssetsUrl(this.icon),
        price = this.price,
        totalPrice = this.totalPrice,
        slotType = slotType,
        rarity = rarity,
        aggressionType = null,
        heroClass = heroClass,
        stats = this.stats.map { it.createStatDetails() },
        effects = this.effects.map {
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

fun NetworkItem.asItemDetails(): ItemDetails {

    val rarity = when (this.rarity) {
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
        id = this.id.toString(),
        gameId = this.gameId ?: 0,
        name = this.name,
        displayName = this.displayName,
        imageSrc = RetrofitHelper.getRankImageUrl(this.image),
        price = this.price ?: 0,
        totalPrice = this.totalPrice,
        slotType = slotType,
        rarity = rarity,
        aggressionType = null,
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
        ItemDescriptionStat.HEALTH -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.max_health,
            name = "Max Health",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.MANA -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.max_mana,
            name = "Max Mana",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.BASE_HEALTH_REGENERATION -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.health_regen,
            name = "Health Regen",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.BASE_MANA_REGENERATION -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.mana_regen,
            name = "Mana Regen",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.PHYSICAL_POWER -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.physical_power,
            name = "Physical Power",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.MAGICAL_POWER -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.magical_power,
            name = "Magical Power",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.ATTACK_SPEED -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.attack_speed,
            name = "Attack Speed",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.PHYSICAL_ARMOR -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.physical_armor,
            name = "Physical Armor",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.MAGICAL_ARMOR -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.magical_armor,
            name = "Magical Armor",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.PHYSICAL_PENETRATION -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.physical_pen,
            name = "Physical Penetration",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.MAGICAL_PENETRATION -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.magical_pen,
            name = "Magical Penetration",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.CRITICAL_CHANCE -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.critical_chance,
            name = "Critical Chance",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.GOLD_PER_SECOND -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.gold_per_second,
            name = "Gold Per Second",
            value = this.value.toStatValue()
        )
        ItemDescriptionStat.TENACITY -> ItemDetails.StatDetails(
            iconId = coreResources.drawable.tenacity,
            name = "Tenacity",
            value = this.value.toStatValue()
        )
        else -> ItemDetails.StatDetails(
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