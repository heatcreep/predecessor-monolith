package com.aowen.monolith.data.database.model

import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.FavoriteBuildListItem

fun BuildListItem.asFavoriteBuildListEntity(): FavoriteBuildListEntity {
    return FavoriteBuildListEntity(
        buildId = id,
        heroId = heroId,
        role = role,
        title = title,
        description = description,
        author = author,
        crestId = crest,
        itemIds = buildItems,
        upvotesCount = upvotes,
        downvotesCount = downvotes,
        createdAt = createdAt,
        gameVersion = version ?: ""
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
