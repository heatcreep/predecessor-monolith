package com.aowen.monolith.data

import com.google.gson.annotations.SerializedName

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

)
