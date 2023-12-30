package com.aowen.monolith.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class BuildDto(
    val id: Int,
    val title: String,
    val description: String?,
    @SerializedName("hero_id")
    val heroId: Int,
    val role: String,
    @SerializedName("crest_id")
    val crestId: Int,
    @SerializedName("item1_id")
    val item1Id: Int,
    @SerializedName("item2_id")
    val item2Id: Int,
    @SerializedName("item3_id")
    val item3Id: Int,
    @SerializedName("item4_id")
    val item4Id: Int,
    @SerializedName("item5_id")
    val item5Id: Int,
    @SerializedName("skill_order")
    val skillOrder: List<Int>?,
    @SerializedName("upvotes_count")
    val upvotesCount: Int,
    @SerializedName("downvotes_count")
    val downvotesCount: Int,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    val author: String,
    val modules: List<ModuleDto>?
)

@Serializable
data class ModuleDto(
    val title: String,
    @SerializedName("item1_id")
    val item1Id: Int?,
    @SerializedName("item2_id")
    val item2Id: Int?,
    @SerializedName("item3_id")
    val item3Id: Int?,
    @SerializedName("item4_id")
    val item4Id: Int?,
    @SerializedName("item5_id")
    val item5Id: Int?,
    @SerializedName("item6_id")
    val item6Id: Int?,

)
