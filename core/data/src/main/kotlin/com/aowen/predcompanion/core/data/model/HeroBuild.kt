package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.database.model.FavoriteBuildListEntity
import com.aowen.predcompanion.core.model.data.FavoriteBuildListItem
import com.aowen.predcompanion.core.model.data.HeroBuild

fun HeroBuild.asFavoriteBuildListItem(): FavoriteBuildListItem {
    return FavoriteBuildListItem(
        buildId = id,
        heroId = heroId,
        role = role,
        title = title,
        description = description,
        author = author,
        crestId = crestId,
        itemIds = buildItemIds,
        upvotesCount = upvotes,
        downvotesCount = downvotes,
        createdAt = createdAt,
        gameVersion = version ?: ""
    )
}

fun HeroBuild.asFavoriteBuildListEntity(): FavoriteBuildListEntity {
    return FavoriteBuildListEntity(
        buildId = id,
        heroId = heroId,
        role = role,
        title = title,
        description = description,
        author = author,
        crestId = crestId,
        itemIds = buildItemIds,
        upvotesCount = upvotes,
        downvotesCount = downvotes,
        createdAt = createdAt,
        gameVersion = version ?: ""
    )
}

fun FavoriteBuildListEntity.asFavoriteBuildListItem(): FavoriteBuildListItem {
    return FavoriteBuildListItem(
        buildId = buildId,
        heroId = heroId,
        role = role,
        title = title,
        description = description,
        author = author,
        crestId = crestId,
        itemIds = itemIds,
        upvotesCount = upvotesCount,
        downvotesCount = downvotesCount,
        createdAt = createdAt,
        gameVersion = gameVersion
    )
}
