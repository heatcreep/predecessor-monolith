package com.aowen.predcompanion.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aowen.predcompanion.core.database.dao.ClaimedPlayerDao
import com.aowen.predcompanion.core.database.dao.FavoriteBuildDao
import com.aowen.predcompanion.core.database.model.ClaimedPlayerEntity
import com.aowen.predcompanion.core.database.model.FavoriteBuildListEntity
import com.aowen.predcompanion.core.database.util.IntListConverter


@Database(
    entities = [
        FavoriteBuildListEntity::class,
        ClaimedPlayerEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    IntListConverter::class
)
abstract class MonolithDatabase : RoomDatabase() {
    abstract fun favoriteBuildListItemDao(): FavoriteBuildDao
    abstract fun claimedPlayerDao(): ClaimedPlayerDao
}