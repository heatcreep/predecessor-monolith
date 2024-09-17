package com.aowen.monolith.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aowen.monolith.data.database.dao.ClaimedPlayerDao
import com.aowen.monolith.data.database.dao.FavoriteBuildDao
import com.aowen.monolith.data.database.model.ClaimedPlayerEntity
import com.aowen.monolith.data.database.model.FavoriteBuildListEntity
import com.aowen.monolith.data.database.util.IntListConverter


@Database(
    entities = [
        FavoriteBuildListEntity::class,
        ClaimedPlayerEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    IntListConverter::class
)
abstract class MonolithDatabase : RoomDatabase() {
    abstract fun favoriteBuildListItemDao(): FavoriteBuildDao
    abstract fun claimedPlayerDao(): ClaimedPlayerDao
}