package com.aowen.monolith.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.aowen.monolith.data.database.model.FavoriteBuildListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteBuildDao {

    @Transaction
    @Query(
        value = "SELECT * FROM favorite_builds"
    )
    fun getFavoriteBuildListItems(): Flow<List<FavoriteBuildListEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteBuildListItem(favoriteBuildListItem: FavoriteBuildListEntity)

    @Query(
        value = """
            DELETE FROM favorite_builds
            WHERE buildId in (:buildIds)
        """
    )
    suspend fun deleteFavoriteBuildListItems(buildIds: List<Int>)

    @Query(
        value = """
            DELETE FROM favorite_builds
        """
    )
    suspend fun deleteAllFavoriteBuildListItems()
}