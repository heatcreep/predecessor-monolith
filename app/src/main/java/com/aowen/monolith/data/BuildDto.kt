package com.aowen.monolith.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BuildDto(
    val id: Int,
    val title: String,
    val description: String?,
    @SerialName("hero_id") val heroId: Int,
    val role: String,
    @SerialName("crest_id")
    val crestId: Int,
    @SerialName("item1_id")
    val item1Id: Int,
    @SerialName("item2_id")
    val item2Id: Int,
    @SerialName("item3_id")
    val item3Id: Int,
    @SerialName("item4_id")
    val item4Id: Int,
    @SerialName("item5_id")
    val item5Id: Int,
    @SerialName("skill_order")
    val skillOrder: List<Int>?,
    @SerialName("upvotes_count")
    val upvotesCount: Int,
    @SerialName("downvotes_count")
    val downvotesCount: Int,
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("updated_at")
    val updatedAt: String?,
    val author: String,
    val modules: List<ModuleDto> = emptyList()
)

@Serializable
data class ModuleDto(
    val title: String,
    @SerialName("item1_id")
    val item1Id: Int?,
    @SerialName("item2_id")
    val item2Id: Int?,
    @SerialName("item3_id")
    val item3Id: Int?,
    @SerialName("item4_id")
    val item4Id: Int?,
    @SerialName("item5_id")
    val item5Id: Int?,
    @SerialName("item6_id")
    val item6Id: Int?,

)
