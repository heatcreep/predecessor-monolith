@file:SuppressLint("UnsafeOptInUsageError")
package com.aowen.predcompanion.core.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkHeroBaseStats(
    @SerialName("max_health")
    val maxHealth: List<Float>,
    @SerialName("base_health_regeneration")
    val healthRegen: List<Float>,
    @SerialName("max_mana")
    val maxMana: List<Float>?,
    @SerialName("base_mana_regeneration")
    val manaRegen: List<Float>?,
    @SerialName("attack_speed")
    val attackSpeed: List<Float>,
    @SerialName("physical_armor")
    val physicalArmor: List<Float>,
    @SerialName("magical_armor")
    val magicalArmor: List<Float>,
    @SerialName("physical_power")
    val physicalPower: List<Float>,
    @SerialName("base_movement_speed")
    val movementSpeed: List<Float>,
    val cleave: List<Float>,
    @SerialName("attack_range")
    val attackRange: List<Float>,
)

@Serializable
data class NetworkHeroAbility(
    @SerialName("display_name")
    val displayName: String,
    val image: String,
    @SerialName("game_description")
    val gameDescription: String,
    @SerialName("menu_description")
    val menuDescription: String?,
    val cooldown: List<Float?>,
    val cost: List<Float>,
)

@Serializable
data class NetworkHero(
    val id: Long,
    val name: String,
    @SerialName("display_name")
    val displayName: String,
    val image: String,
    val stats: List<Int>,
    val classes: List<String>,
    val roles: List<String>,
    val abilities: List<NetworkHeroAbility>,
    @SerialName("base_stats")
    val baseStats: NetworkHeroBaseStats,
)
