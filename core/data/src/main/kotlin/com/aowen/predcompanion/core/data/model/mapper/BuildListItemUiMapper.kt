package com.aowen.predcompanion.core.data.model.mapper

import com.aowen.predcompanion.core.data.repository.items.ItemRepository
import com.aowen.predcompanion.core.database.model.FavoriteBuildListEntity
import com.aowen.predcompanion.core.model.data.FavoriteBuildListItem
import com.aowen.predcompanion.core.model.data.ItemDetails
import com.aowen.predcompanion.data.BuildListItem
import com.aowen.predcompanion.data.FavoriteBuildDto
import com.aowen.predcompanion.data.ItemModule
import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject

data class ItemModuleUi(
    val id: String? = null,
    val title: String = "",
    val items: List<ItemDetails> = emptyList(),
)

data class BuildUiListItem(
    val buildId: Int,
    val userId: String? = null,
    val title: String,
    val author: String,
    val role: String,
    val description: String? = null,
    val heroId: Long,
    val crest: ItemDetails = ItemDetails(),
    val buildItems: List<ItemDetails> = emptyList(),
    val skillOrder: List<Int>? = null,
    val netVotes: Int = 0,
    val upvotes: Int = 0,
    val downvotes: Int = 0,
    val modules: List<ItemModuleUi> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val version: String? = null
)


class BuildListItemUiMapper @Inject constructor(
    itemRepository: ItemRepository
) {

    val items = itemRepository.allItems.value

    fun buildFrom(
        buildListItem: BuildListItem,
    ): BuildUiListItem {
        return BuildUiListItem(
            buildId = buildListItem.id,
            userId = buildListItem.userId,
            title = buildListItem.title,
            author = buildListItem.author,
            role = buildListItem.role,
            description = buildListItem.description,
            heroId = buildListItem.heroId,
            crest = items[buildListItem.crestId] ?: ItemDetails(),
            buildItems = buildListItem.buildItemIds.mapNotNull { items[it] },
            skillOrder = buildListItem.skillOrder,
            netVotes = buildListItem.netVotes,
            upvotes = buildListItem.upvotes,
            downvotes = buildListItem.downvotes,
            modules = buildListItem.modules.map { it.toUi(items) },
            createdAt = buildListItem.createdAt,
            updatedAt = buildListItem.updatedAt,
            version = buildListItem.version
        )
    }

    fun buildFrom(
        favoriteBuildListItem: FavoriteBuildListItem,
    ): BuildUiListItem {
        return BuildUiListItem(
            buildId = favoriteBuildListItem.buildId,
            title = favoriteBuildListItem.title,
            author = favoriteBuildListItem.author,
            role = favoriteBuildListItem.role,
            description = favoriteBuildListItem.description,
            heroId = favoriteBuildListItem.heroId,
            crest = items[favoriteBuildListItem.crestId] ?: ItemDetails(),
            buildItems = favoriteBuildListItem.itemIds.mapNotNull { items[it] },
            upvotes = favoriteBuildListItem.upvotesCount,
            downvotes = favoriteBuildListItem.downvotesCount,
            createdAt = favoriteBuildListItem.createdAt,
            version = favoriteBuildListItem.gameVersion
        )
    }
}

fun BuildUiListItem.asFavoriteBuildDto(userId: UUID): FavoriteBuildDto {
    return FavoriteBuildDto(
        id = UUID.randomUUID(),
        createdAt = Timestamp(System.currentTimeMillis()).toString(),
        userId = userId,
        buildId = buildId,
        heroId = heroId,
        role = role,
        title = title,
        description = description,
        author = author,
        crestId = crest.id,
        itemIds = buildItems.map { it.id },
        upvotesCount = upvotes,
        downvotesCount = downvotes,
        gameVersion = version ?: ""
    )
}

fun BuildUiListItem.asFavoriteBuildListEntity(): FavoriteBuildListEntity {
    return FavoriteBuildListEntity(
        buildId = buildId,
        heroId = heroId,
        role = role,
        title = title,
        description = description,
        author = author,
        crestId = crest.id,
        itemIds = buildItems.map { it.id },
        upvotesCount = upvotes,
        downvotesCount = downvotes,
        createdAt = createdAt,
        gameVersion = version ?: ""
    )
}

private fun ItemModule.toUi(items: Map<Int, ItemDetails>): ItemModuleUi =
    ItemModuleUi(
        id = id,
        title = title,
        items = itemIds.mapNotNull { items[it] },
    )
