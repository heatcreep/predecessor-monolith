package com.aowen.monolith.data

import com.aowen.monolith.R
import com.aowen.monolith.network.RetrofitHelper

data class EffectDetails(
    val name: String = "",
    val active: Boolean = false,
    val gameDescription: String? = "",
    val menuDescription: String? = "",
)


data class StatDetails(
    val iconId: Int,
    val name: String = "",
    val value: String = "",
)

fun Double.toStatValue(): String {
    val isWholeNumber = this == this.toLong().toDouble()
    return if (isWholeNumber) {
        this.toInt().toString()
    } else {
        this.toFloat().toPercentageString()
    }
}

fun Map<String, Double>?.createStatDetails(): List<StatDetails> {
    val listOfStats = mutableListOf<StatDetails>()
    for ((key, value) in this ?: emptyMap()) {
        val statDetails = when (key) {
            "max_health" -> StatDetails(
                iconId = R.drawable.max_health,
                name = "Max Health",
                value = value.toStatValue()
            )

            "max_mana" -> StatDetails(
                iconId = R.drawable.max_mana,
                name = "Max Mana",
                value = value.toStatValue()
            )

            "health_regeneration" -> StatDetails(
                iconId = R.drawable.health_regen,
                name = "Health Regen",
                value = value.toStatValue()
            )

            "mana_regeneration" -> StatDetails(
                iconId = R.drawable.mana_regen,
                name = "Mana Regen",
                value = value.toStatValue()
            )

            "physical_power" -> StatDetails(
                iconId = R.drawable.physical_power,
                name = "Physical Power",
                value = value.toStatValue()
            )

            "magical_power" -> StatDetails(
                iconId = R.drawable.magical_power,
                name = "Magical Power",
                value = value.toStatValue()
            )

            "attack_speed" -> StatDetails(
                iconId = R.drawable.attack_speed,
                name = "Attack Speed",
                value = value.toStatValue()
            )

            "physical_armor" -> StatDetails(
                iconId = R.drawable.physical_armor,
                name = "Physical Armor",
                value = value.toStatValue()
            )

            "magical_armor" -> StatDetails(
                iconId = R.drawable.magical_armor,
                name = "Magical Armor",
                value = value.toStatValue()
            )

            "heal_shield_increase" -> StatDetails(
                iconId = R.drawable.heal_shield_increase,
                name = "Heal and Shield Increase",
                value = value.toStatValue()
            )

            "ability_haste" -> StatDetails(
                iconId = R.drawable.ability_haste,
                name = "Ability Haste",
                value = value.toStatValue()
            )

            "lifesteal" -> StatDetails(
                iconId = R.drawable.lifesteal,
                name = "Lifesteal",
                value = value.toStatValue()
            )

            "magical_lifesteal" -> StatDetails(
                iconId = R.drawable.magical_lifesteal,
                name = "Magical Lifesteal",
                value = value.toStatValue()
            )

            "omnivamp" -> StatDetails(
                iconId = R.drawable.omnivamp,
                name = "Omnivamp",
                value = value.toStatValue()
            )

            "movement_speed" -> StatDetails(
                iconId = R.drawable.movement_speed,
                name = "Movement Speed",
                value = value.toStatValue()
            )

            "physical_penetration" -> StatDetails(
                iconId = R.drawable.physical_pen,
                name = "Physical Penetration",
                value = value.toStatValue()
            )

            "magical_penetration" -> StatDetails(
                iconId = R.drawable.magical_pen,
                name = "Magical Penetration",
                value = value.toStatValue()
            )

            "critical_chance" -> StatDetails(
                iconId = R.drawable.critical_chance,
                name = "Critical Chance",
                value = value.toStatValue()
            )

            "gold_per_second" -> StatDetails(
                iconId = R.drawable.gold_per_second,
                name = "Gold Per Second",
                value = value.toStatValue()
            )

            "tenacity" -> StatDetails(
                iconId = R.drawable.tenacity,
                name = "Tenacity",
                value = value.toStatValue()
            )

            else -> StatDetails(
                iconId = coil.singleton.R.drawable.ic_100tb,
                name = "Unknown",
                value = value.toStatValue()
            )

        }
        listOfStats.add(statDetails)
    }
    return listOfStats

}


fun EffectDto.create(): EffectDetails {
    return EffectDetails(
        name = this.name,
        active = this.active,
        gameDescription = this.gameDescription,
        menuDescription = this.menuDescription,
    )
}


data class ItemDetails(
    val id: Int = 0,
    val gameId: Int = 0,
    val name: String = "",
    val displayName: String = "",
    val image: String = "",
    val price: Int? = 0,
    val totalPrice: Int = 0,
    val slotType: SlotType = SlotType.PASSIVE,
    val rarity: Rarity = Rarity.COMMON,
    val aggressionType: String? = null,
    val heroClass: String? = null,
    val requiredLevel: Int? = null,
    val stats: List<StatDetails> = emptyList(),
    val effects: List<EffectDetails?> = emptyList(),
    val requirements: List<String?> = emptyList(),
    val buildPath: List<String?> = emptyList(),
)

fun ItemDto.create(): ItemDetails {

    val rarity = when (this.rarity) {
        "Common" -> Rarity.COMMON
        "Uncommon" -> Rarity.UNCOMMON
        "Rare" -> Rarity.RARE
        "Epic" -> Rarity.EPIC
        "Legendary" -> Rarity.LEGENDARY
        else -> Rarity.COMMON
    }

    val slotType = when (this.slotType) {
        "Trinket" -> SlotType.TRINKET
        "Crest" -> SlotType.CREST
        "Active" -> SlotType.ACTIVE
        else -> SlotType.PASSIVE
    }
    return ItemDetails(
        id = this.id,
        gameId = this.gameId,
        name = this.name,
        displayName = this.displayName,
        image = RetrofitHelper.getRankImageUrl(this.image),
        price = this.price ?: 0,
        totalPrice = this.totalPrice,
        slotType = slotType,
        rarity = rarity,
        aggressionType = this.aggressionType,
        heroClass = this.heroClass,
        requiredLevel = this.requiredLevel,
        stats = this.stats.createStatDetails(),
        effects = this.effects.map { it?.create() },
        requirements = this.requirements,
        buildPath = this.buildPath,
    )
}

fun Float.toPercentageString(): String {
    return "${(this * 100).toInt()}%"
}


enum class Rarity(val value: String) {
    COMMON("Tier I"),
    UNCOMMON("Tier I"),
    RARE("Tier II"),
    EPIC("Tier III"),
    LEGENDARY("Tier III"),
}

enum class SlotType(val value: String) {
    TRINKET("Trinket"),
    CREST("Crest"),
    ACTIVE("Active"),
    PASSIVE("Passive")
}