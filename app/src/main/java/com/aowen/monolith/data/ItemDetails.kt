package com.aowen.monolith.data

import com.aowen.monolith.network.RetrofitHelper

data class EffectDetails(
    val name: String = "",
    val active: Boolean = false,
    val gameDescription: String? = "",
    val menuDescription: String? = "",
)

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
    val price: Int? = null,
    val totalPrice: Int = 0,
    val slotType: String? = "",
    val rarity: Rarity = Rarity.COMMON,
    val aggressionType: String? = null,
    val heroClass: String? = null,
    val requiredLevel: Int? = null,
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
    return ItemDetails(
        id = this.id,
        gameId = this.gameId,
        name = this.name,
        displayName = this.displayName,
        image = RetrofitHelper.getRankImageUrl(this.image),
        price = this.price,
        totalPrice = this.totalPrice,
        slotType = this.slotType,
        rarity = rarity,
        aggressionType = this.aggressionType,
        heroClass = this.heroClass,
        requiredLevel = this.requiredLevel,
        effects = this.effects.map { it?.create() },
        requirements = this.requirements,
        buildPath = this.buildPath,
    )
}


enum class Rarity(val value: String) {
    COMMON("Common"),
    UNCOMMON("Uncommon"),
    RARE("Rare"),
    EPIC("Epic"),
    LEGENDARY("Legendary"),
}