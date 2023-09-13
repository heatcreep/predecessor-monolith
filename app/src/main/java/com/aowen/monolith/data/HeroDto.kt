package com.aowen.monolith.data

import com.google.gson.annotations.SerializedName

data class BaseStatsDto(
    @SerializedName("max_health")
    val maxHealth: List<Float>,
    @SerializedName("base_health_regeneration")
    val healthRegen: List<Float>,
    @SerializedName("max_mana")
    val maxMana: List<Float>,
    @SerializedName("base_mana_regeneration")
    val manaRegen: List<Float>,
    @SerializedName("attack_speed")
    val attackSpeed: List<Float>,
    @SerializedName("physical_armor")
    val physicalArmor: List<Float>,
    @SerializedName("magical_armor")
    val magicalArmor: List<Float>,
    @SerializedName("physical_power")
    val physicalPower: List<Float>,
    @SerializedName("base_movement_speed")
    val movementSpeed: List<Float>,
    val cleave: List<Float>,
    @SerializedName("attack_range")
    val attackRange: List<Float>,
)

data class AbilityDto(
    @SerializedName("display_name")
    val displayName: String,
    val image: String,
    @SerializedName("game_description")
    val gameDescription: String,
    @SerializedName("menu_description")
    val menuDescription: String?,
    val cooldown: List<Float?>,
    val cost: List<Int>,
)

data class HeroDto(
    val id: String,
    val name: String,
    @SerializedName("display_name")
    val displayName: String,
    val stats: List<Int>,
    val classes: List<String>,
    val roles: List<String>,
    val abilities: List<AbilityDto>,
    @SerializedName("base_stats")
    val baseStats: BaseStatsDto,

)
