package com.aowen.predcompanion.data

import com.aowen.predcompanion.core.model.data.FavoriteBuildListItem
import java.sql.Timestamp
import java.util.UUID

data class BuildListItem(
    val id: Int = 0,
    val userId: String? = null,
    val title: String = "",
    val author: String = "",
    val role: String = "unknown",
    val description: String? = "",
    val heroId: Long = 999,
    val crestId: Int = 0,
    val buildItemIds: List<Int> = emptyList(),
    val skillOrder: List<Int>? = null,
    val netVotes: Int = 0,
    val upvotes: Int = 0,
    val downvotes: Int = 0,
    val modules: List<ItemModule> = emptyList(),
    val createdAt: String? = "",
    val updatedAt: String? = "",
    val version: String? = ""
)

data class ItemModule(
    val id: String? = UUID.randomUUID().toString(),
    val title: String = "",
    val itemIds: List<Int> = emptyList(),
)


fun ModuleDto.toItemModule(): ItemModule {
    return ItemModule(
        id = id,
        title = title,
        itemIds = listOfNotNull(item1Id, item2Id, item3Id, item4Id, item5Id, item6Id),
    )
}

fun BuildListItem.asFavoriteBuildListItem(): FavoriteBuildListItem {
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


