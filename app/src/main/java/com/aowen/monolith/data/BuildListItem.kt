package com.aowen.monolith.data

import com.aowen.monolith.data.database.model.FavoriteBuildListEntity
import java.sql.Timestamp
import java.util.UUID


data class BuildListItem(
    val id: Int = 0,
    val userId: String? = null,
    val title: String = "",
    val author: String = "",
    val role: String = "unknown",
    val description: String? = "",
    val heroId: Int = 999,
    val crest: Int = 0,
    val buildItems: List<Int> = emptyList(),
    val skillOrder: List<Int>? = null,
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
    val items: List<Int> = emptyList(),
)

fun BuildDto.create(): BuildListItem {
    return BuildListItem(
        id = id,
        title = title,
        author = author,
        role = role,
        description = description,
        heroId = heroId,
        crest = crestId,
        buildItems = listOfNotNull(item1Id, item2Id, item3Id, item4Id, item5Id, item6Id),
        skillOrder = skillOrder,
        upvotes = upvotesCount,
        downvotes = downvotesCount,
        createdAt = createdAt,
        updatedAt = updatedAt,
        modules = modules.map { it.create() },
        version = gameVersion.name
    )
}

fun ModuleDto.create(): ItemModule {
    return ItemModule(
        id = id,
        title = title,
        items = listOfNotNull(item1Id, item2Id, item3Id, item4Id, item5Id, item6Id),
    )
}

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

fun BuildListItem.asFavoriteBuildDto(userId: UUID): FavoriteBuildDto {
    return FavoriteBuildDto(
        id = UUID.randomUUID(),
        createdAt = Timestamp(System.currentTimeMillis()).toString(),
        userId = userId,
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
        gameVersion = version ?: ""
    )
}
