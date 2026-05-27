package com.aowen.predcompanion.core.data.repository

import com.aowen.predcompanion.core.data.model.toItemModule
import com.aowen.predcompanion.core.database.model.FavoriteBuildListEntity
import com.aowen.predcompanion.core.model.data.FavoriteBuildListItem
import com.aowen.predcompanion.core.model.data.HeroBuild
import com.aowen.predcompanion.core.network.model.NetworkFavoriteHeroBuild
import com.aowen.predcompanion.core.network.model.NetworkHeroBuild
import javax.inject.Inject

class BuildListItemDataMapper @Inject constructor() {

    fun createBuildListItem(networkHeroBuild: NetworkHeroBuild): HeroBuild {
        val allItemIds = listOfNotNull(
            networkHeroBuild.item1Id,
            networkHeroBuild.item2Id,
            networkHeroBuild.item3Id,
            networkHeroBuild.item4Id,
            networkHeroBuild.item5Id,
            networkHeroBuild.item6Id
        )
        return HeroBuild(
            id = networkHeroBuild.id,
            title = networkHeroBuild.title,
            author = networkHeroBuild.author,
            role = networkHeroBuild.role,
            description = networkHeroBuild.description,
            heroId = networkHeroBuild.heroId,
            crestId = networkHeroBuild.crestId,
            buildItemIds = allItemIds,
            skillOrder = networkHeroBuild.skillOrder,
            netVotes = networkHeroBuild.upvotesCount - networkHeroBuild.downvotesCount,
            upvotes = networkHeroBuild.upvotesCount,
            downvotes = networkHeroBuild.downvotesCount,
            createdAt = networkHeroBuild.createdAt,
            updatedAt = networkHeroBuild.updatedAt,
            modules = networkHeroBuild.modules.map { it.toItemModule() },
            version = networkHeroBuild.gameVersion.name
        )
    }

    fun createFavoriteBuildListItemFrom(buildDto: NetworkFavoriteHeroBuild): FavoriteBuildListItem {
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
