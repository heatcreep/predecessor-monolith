package com.aowen.monolith.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aowen.monolith.data.BuildListItem
import com.aowen.monolith.data.FavoriteBuildListItem

@Entity(
    tableName = "favorite_builds"
)
data class FavoriteBuildListEntity(
    @PrimaryKey
    val buildId: Int,
    val heroId: Int,
    val role: String,
    val title: String,
    @ColumnInfo(defaultValue = "")
    val description: String?,
    val author: String,
    val crestId: Int,
    val itemIds: List<Int>,
    val upvotesCount: Int,
    val downvotesCount: Int,
    @ColumnInfo(defaultValue = "")
    val createdAt: String?,
    val gameVersion: String
)

fun FavoriteBuildListEntity.asBuildListItem(): BuildListItem {
    return BuildListItem(
        id = this.buildId,
        title = this.title,
        author = this.author,
        role = this.role,
        description = this.description,
        heroId = this.heroId,
        crest = this.crestId,
        buildItems = this.itemIds,
        upvotes = this.upvotesCount,
        downvotes = this.downvotesCount,
        createdAt = this.createdAt,
        version = this.gameVersion
    )
}

fun FavoriteBuildListEntity.asFavoriteBuildListItem(): FavoriteBuildListItem {
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