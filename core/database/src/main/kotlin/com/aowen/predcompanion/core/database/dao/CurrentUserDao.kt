package com.aowen.predcompanion.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.aowen.predcompanion.core.database.model.CurrentUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentUserDao {

    @Query("SELECT * FROM current_users LIMIT 1")
    fun getCurrentUser(): Flow<CurrentUserEntity?>

    @Upsert
    suspend fun upsertCurrentUser(user: CurrentUserEntity)

    @Query("DELETE FROM current_users")
    suspend fun deleteCurrentUser()
}
