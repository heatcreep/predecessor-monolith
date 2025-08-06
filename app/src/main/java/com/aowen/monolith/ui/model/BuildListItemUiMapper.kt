package com.aowen.monolith.ui.model

import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.FavoriteBuildListItem
import com.aowen.monolith.data.ItemModule
import javax.inject.Inject


sealed class BuildUiListItem(
    open val buildId: Int,
    open val userId: String?,
    open val title: String,
    open val author: String,
    open val role: String,
    open val description: String?,
    open val heroId: Long,
    open val crest: Int,
    open val buildItems: List<Int>,
    open val skillOrder: List<Int>?,
    open val netVotes: Int,
    open val upvotes: Int,
    open val downvotes: Int,
    open val modules: List<ItemModule>,
    open val createdAt: String?,
    open val updatedAt: String?,
    open val version: String?
) {

    data class NormalBuildUiListItem(
        override val buildId: Int,
        override val userId: String? = null,
        override val title: String,
        override val author: String,
        override val role: String,
        override val description: String? = null,
        override val heroId: Long,
        override val crest: Int = 0,
        override val buildItems: List<Int> = emptyList(),
        override val skillOrder: List<Int>? = null,
        override val netVotes: Int = 0,
        override val upvotes: Int = 0,
        override val downvotes: Int = 0,
        override val modules: List<ItemModule> = emptyList(),
        override val createdAt: String? = null,
        override val updatedAt: String? = null,
        override val version: String? = null
    ) : BuildUiListItem(
        buildId, userId, title, author, role, description, heroId, crest, buildItems, skillOrder,
        netVotes, upvotes, downvotes, modules, createdAt, updatedAt, version
    )

    data class FavoriteBuildUiListItem(
        override val buildId: Int,
        override val userId: String? = null,
        override val title: String,
        override val author: String,
        override val role: String,
        override val description: String? = null,
        override val heroId: Long,
        override val crest: Int = 0,
        override val buildItems: List<Int> = emptyList(),
        override val skillOrder: List<Int>? = null,
        override val netVotes: Int = 0,
        override val upvotes: Int = 0,
        override val downvotes: Int = 0,
        override val modules: List<ItemModule> = emptyList(),
        override val createdAt: String? = null,
        override val updatedAt: String? = null,
        override val version: String? = null
    ) : BuildUiListItem(
        buildId, userId, title, author, role, description, heroId, crest, buildItems, skillOrder,
        netVotes, upvotes, downvotes, modules, createdAt, updatedAt, version
    )
}

class BuildListItemUiMapper @Inject constructor() {

    fun buildFrom(buildListItem: BuildListItem): BuildUiListItem.NormalBuildUiListItem {
        return BuildUiListItem.NormalBuildUiListItem(
            buildId = buildListItem.id,
            userId = buildListItem.userId,
            title = buildListItem.title,
            author = buildListItem.author,
            role = buildListItem.role,
            description = buildListItem.description,
            heroId = buildListItem.heroId,
            crest = buildListItem.crest,
            buildItems = buildListItem.buildItems,
            skillOrder = buildListItem.skillOrder,
            netVotes = buildListItem.netVotes,
            upvotes = buildListItem.upvotes,
            downvotes = buildListItem.downvotes,
            modules = buildListItem.modules,
            createdAt = buildListItem.createdAt,
            updatedAt = buildListItem.updatedAt,
            version = buildListItem.version
        )
    }

    fun buildFrom(favoriteBuildListItem: FavoriteBuildListItem): BuildUiListItem.FavoriteBuildUiListItem {
        return BuildUiListItem.FavoriteBuildUiListItem(
            buildId = favoriteBuildListItem.buildId,
            title = favoriteBuildListItem.title,
            author = favoriteBuildListItem.author,
            role = favoriteBuildListItem.role,
            description = favoriteBuildListItem.description,
            heroId = favoriteBuildListItem.heroId,
            crest = favoriteBuildListItem.crestId,
            buildItems = favoriteBuildListItem.itemIds,
            upvotes = favoriteBuildListItem.upvotesCount,
            downvotes = favoriteBuildListItem.downvotesCount,
            createdAt = favoriteBuildListItem.createdAt,
            version = favoriteBuildListItem.gameVersion
        )
    }
}