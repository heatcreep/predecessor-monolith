package com.aowen.monolith.data

data class FavoriteBuildListItem(
    val buildId: Int,
    val heroId: Int,
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

fun FavoriteBuildDto.create(): FavoriteBuildListItem {
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


