package com.aowen.monolith.data

import com.google.gson.annotations.SerializedName

data class EffectDto(
    val name: String = "",
    val active: Boolean = false,
    @SerializedName("game_description")
    val gameDescription: String? = "",
    @SerializedName("menu_description")
    val menuDescription: String? = "",
)

data class ItemDto(
    val id: Int = 0,
    @SerializedName("game_id")
    val gameId: Int = 0,
    val name: String = "",
    @SerializedName("display_name")
    val displayName: String = "",
    val image: String = "",
    val price: Int? = null,
    @SerializedName("total_price")
    val totalPrice: Int = 0,
    @SerializedName("slot_type")
    val slotType: String? = null,
    val rarity: String? = null,
    @SerializedName("aggression_type")
    val aggressionType: String? = null,
    @SerializedName("hero_class")
    val heroClass: String? = null,
    @SerializedName("required_level")
    val requiredLevel: Int? = null,
    val effects: List<EffectDto?> = emptyList(),
    val requirements: List<String?> = emptyList(),
    @SerializedName("build_path")
    val buildPath: List<String?> = emptyList(),
)
