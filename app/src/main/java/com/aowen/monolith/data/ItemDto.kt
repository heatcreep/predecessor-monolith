package com.aowen.monolith.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EffectDto(
    val name: String = "",
    val active: Boolean = false,
    @SerialName("game_description")
    val gameDescription: String? = "",
    @SerialName("menu_description")
    val menuDescription: String? = "",
)

@Serializable
data class ItemDto(
    val id: Int = 0,
    @SerialName("game_id")
    val gameId: Int = 0,
    val name: String = "",
    @SerialName("display_name")
    val displayName: String = "",
    val image: String = "",
    val price: Int? = null,
    @SerialName("total_price")
    val totalPrice: Int = 0,
    @SerialName("slot_type")
    val slotType: String? = null,
    val rarity: String? = null,
    @SerialName("aggression_type")
    val aggressionType: String? = null,
    @SerialName("hero_class")
    val heroClass: String? = null,
    @SerialName("required_level")
    val requiredLevel: Int? = null,
    val stats: Map<String, Double>? = null,
    val effects: List<EffectDto?> = emptyList(),
    val requirements: List<String?> = emptyList(),
    @SerialName("build_path")
    val buildPath: List<String?> = emptyList(),
)
