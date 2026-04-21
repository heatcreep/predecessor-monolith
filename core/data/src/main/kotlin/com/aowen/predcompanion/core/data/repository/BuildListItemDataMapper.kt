package com.aowen.predcompanion.core.data.repository

import com.aowen.predcompanion.core.database.model.FavoriteBuildListEntity
import com.aowen.predcompanion.core.model.data.FavoriteBuildListItem
import com.aowen.predcompanion.data.BuildDto
import com.aowen.predcompanion.data.BuildListItem
import com.aowen.predcompanion.data.FavoriteBuildDto
import com.aowen.predcompanion.data.toItemModule
import javax.inject.Inject

class BuildListItemDataMapper @Inject constructor() {

    fun createBuildListItem(buildDto: BuildDto): BuildListItem {
        val allItemIds = listOfNotNull(
            buildDto.item1Id,
            buildDto.item2Id,
            buildDto.item3Id,
            buildDto.item4Id,
            buildDto.item5Id,
            buildDto.item6Id
        )
        return BuildListItem(
            id = buildDto.id,
            title = buildDto.title,
            author = buildDto.author,
            role = buildDto.role,
            description = buildDto.description,
            heroId = buildDto.heroId,
            crestId = buildDto.crestId,
            buildItemIds = allItemIds,
            skillOrder = buildDto.skillOrder,
            netVotes = buildDto.upvotesCount - buildDto.downvotesCount,
            upvotes = buildDto.upvotesCount,
            downvotes = buildDto.downvotesCount,
            createdAt = buildDto.createdAt,
            updatedAt = buildDto.updatedAt,
            modules = buildDto.modules.map { it.toItemModule() },
            version = buildDto.gameVersion.name
        )
    }

    fun createFavoriteBuildListItemFrom(buildDto: FavoriteBuildDto): FavoriteBuildListItem {
        return FavoriteBuildListItem(
            buildId = buildDto.buildId,
            heroId = buildDto.heroId,
            role = buildDto.role,
            title = buildDto.title,
            description = buildDto.description,
            author = buildDto.author,
            crestId = buildDto.crestId,
            itemIds = buildDto.itemIds,
            upvotesCount = buildDto.upvotesCount,
            downvotesCount = buildDto.downvotesCount,
            createdAt = buildDto.createdAt,
            gameVersion = buildDto.gameVersion
        )
    }

    fun createFavoriteBuildListItemFrom(buildListEntity: FavoriteBuildListEntity): FavoriteBuildListItem {
        return FavoriteBuildListItem(
            buildId = buildListEntity.buildId,
            heroId = buildListEntity.heroId,
            role = buildListEntity.role,
            title = buildListEntity.title,
            description = buildListEntity.description,
            author = buildListEntity.author,
            crestId = buildListEntity.crestId,
            itemIds = buildListEntity.itemIds,
            upvotesCount = buildListEntity.upvotesCount,
            downvotesCount = buildListEntity.downvotesCount,
            createdAt = buildListEntity.createdAt,
            gameVersion = buildListEntity.gameVersion
        )
    }
}
