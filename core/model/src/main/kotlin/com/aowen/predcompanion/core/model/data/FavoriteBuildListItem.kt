package com.aowen.predcompanion.core.model.data

data class FavoriteBuildListItem(
    val buildId: Int,
    val heroId: Long,
    val role: String,
    val title: String,
    val description: String?,
    val author: String,
    val crestId: Int,
    val itemIds: List<Int>,
    val upvotesCount: Int,
    val downvotesCount: Int,
    val createdAt: String?,
    val gameVersion: String
)
