package com.aowen.predcompanion.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aowen.predcompanion.core.database.dao.CurrentUserDao
import com.aowen.predcompanion.core.database.model.CurrentUserEntity
import com.aowen.predcompanion.core.database.util.IntListConverter
import com.aowen.predcompanion.core.database.util.PlayerEntityListConverter


@Database(
    entities = [
        CurrentUserEntity::class,
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    IntListConverter::class,
    PlayerEntityListConverter::class,
)
abstract class MonolithDatabase : RoomDatabase() {
    abstract fun currentUserDao(): CurrentUserDao
}