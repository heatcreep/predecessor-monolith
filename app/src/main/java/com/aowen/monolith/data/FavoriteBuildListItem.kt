package com.aowen.monolith.data

import com.aowen.monolith.data.database.model.FavoriteBuildListEntity

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

fun FavoriteBuildDto.asFavoriteBuildListItem(): FavoriteBuildListItem {
    return FavoriteBuildListItem(
        buildId = this.buildId,
        heroId = this.heroId,
        role = this.role,
        title = this.title,
        description = this.description,
        author = this.author,
        crestId = this.crestId,
        itemIds = this.itemIds,
        upvotesCount = this.upvotesCount,
        downvotesCount = this.downvotesCount,
        createdAt = this.createdAt,
        gameVersion = this.gameVersion
    )
}

fun FavoriteBuildListItem.asFavoriteBuildListEntity(): FavoriteBuildListEntity {
    return FavoriteBuildListEntity(
        buildId = this.buildId,
        heroId = this.heroId,
        role = this.role,
        title = this.title,
        description = this.description,
        author = this.author,
        crestId = this.crestId,
        itemIds = this.itemIds,
        upvotesCount = this.upvotesCount,
        downvotesCount = this.downvotesCount,
        createdAt = this.createdAt,
        gameVersion = this.gameVersion
    )
}


