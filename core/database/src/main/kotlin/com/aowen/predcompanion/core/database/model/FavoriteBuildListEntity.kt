package com.aowen.predcompanion.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_builds"
)
data class FavoriteBuildListEntity(
    @PrimaryKey
    val buildId: Int,
    val heroId: Long,
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