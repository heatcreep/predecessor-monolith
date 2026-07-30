package com.aowen.predcompanion.core.data.model

import com.aowen.predcompanion.core.database.model.FavoriteBuildListEntity
import com.aowen.predcompanion.core.model.data.FavoriteBuildListItem
import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.network.model.NetworkFavoriteHeroBuild
import com.aowen.predcompanion.core.network.model.NetworkHeroBuild
import com.aowen.predcompanion.core.network.model.NetworkHeroBuildModule
import java.sql.Timestamp
import java.util.UUID

fun NetworkHeroBuildModule.toItemModule(): HeroBuild.ItemModule {
    return HeroBuild.ItemModule(
        id = id,
        title = title,
        itemIds = listOfNotNull(item1Id, item2Id, item3Id, item4Id, item5Id, item6Id),
    )
}

fun NetworkHeroBuild.asBuildListItem(): HeroBuild {
    val allItemIds = listOfNotNull(
        item1Id,
        item2Id,
        item3Id,
        item4Id,
        item5Id,
        item6Id
    )
    return HeroBuild(
        id = id,
        title = title,
        author = author,
        role = role,
        description = description,
        heroId = heroId,
        crestId = crestId,
        buildItemIds = allItemIds,
        skillOrder = skillOrder,
        netVotes = upvotesCount - downvotesCount,
        upvotes = upvotesCount,
        downvotes = downvotesCount,
        createdAt = createdAt,
        updatedAt = updatedAt,
        modules = modules.map { it.toItemModule() },
        version = gameVersion.name
    )
}


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

fun NetworkFavoriteHeroBuild.asFavoriteBuildListItem(): FavoriteBuildListItem {
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

fun HeroBuild.asNetworkFavoriteBuild(userId: UUID): NetworkFavoriteHeroBuild {
    return NetworkFavoriteHeroBuild(
        id = UUID.randomUUID(),
        createdAt = Timestamp(System.currentTimeMillis()).toString(),
        userId = userId,
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
        gameVersion = version ?: "",
    )
}