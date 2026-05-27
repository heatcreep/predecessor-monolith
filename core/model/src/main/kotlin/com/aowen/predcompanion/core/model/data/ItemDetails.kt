package com.aowen.predcompanion.core.model.data

data class ItemDetails(
    val id: Int = 0,
    val gameId: Int = 0,
    val name: String = "",
    val displayName: String = "",
    val imageSrc: String = "",
    val price: Int? = 0,
    val totalPrice: Int = 0,
    val slotType: SlotType = SlotType.PASSIVE,
    val rarity: Rarity = Rarity.COMMON,
    val aggressionType: String? = null,
    val heroClass: HeroClass = HeroClass.UNKNOWN,
    val requiredLevel: Int? = null,
    val stats: List<StatDetails> = emptyList(),
    val effects: List<EffectDetails?> = emptyList(),
    val requirements: List<String?> = emptyList(),
    val buildPath: List<String?> = emptyList(),
) {
    data class EffectDetails(
        val name: String = "",
        val active: Boolean = false,
        val condition: String? = "",
        val cooldown: String? = "",
        val menuDescription: String? = "",
    )


    data class StatDetails(
        val iconId: Int,
        val name: String = "",
        val value: String = "",
    )
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