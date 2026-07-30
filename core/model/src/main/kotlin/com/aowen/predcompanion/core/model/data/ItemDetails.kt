package com.aowen.predcompanion.core.model.data

data class ItemDetails(
    val id: String = "",
    val gameId: String = "",
    val name: String = "",
    val displayName: String = "",
    val imageSrc: String = "",
    val price: Int? = 0,
    val totalPrice: Int = 0,
    val slotType: SlotType = SlotType.PASSIVE,
    val rarity: Rarity = Rarity.COMMON,
    val aggressionType: AggressionType? = null,
    val heroClass: HeroClass = HeroClass.UNKNOWN,
    val requiredLevel: Int? = null,
    val stats: List<StatDetails> = emptyList(),
    val effects: List<EffectDetails?> = emptyList(),
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

enum class AggressionType(val value: String) {
    ANTI_BURST("Anti Burst"),
    ANTI_CRIT("Anti Crit"),
    ANTI_DISRUPTION("Anti Disruption"),
    ANTI_HEAL("Anti Heal"),
    ANTI_MAGIC("Anti Magic"),
    ANTI_SHIELD("Anti Shield"),
    ANTI_TANK("Anti Tank"),
    AREA_DAMAGE("Area Damage"),
    ATTACK_SPEED("Attack Speed"),
    BURST("Burst"),
    CLEANSE("Cleanse"),
    CRIPPLE("Cripple"),
    CRIT_AMP("Crit Amp"),
    CURSED("Cursed"),
    DAMAGE_AMP("Damage Amp"),
    DEFENSE("Defense"),
    DEFENSIVE_AURA("Defensive Aura"),
    DISRUPTION("Disruption"),
    DUELING("Dueling"),
    ENGAGE("Engage"),
}