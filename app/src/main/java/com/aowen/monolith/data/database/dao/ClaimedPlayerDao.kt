package com.aowen.monolith.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.aowen.monolith.data.database.model.ClaimedPlayerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Dao
interface ClaimedPlayerDao {

    @Transaction
    @Query(
        value = """
            SELECT playerId FROM claimed_players
        """
    )
    fun getClaimedPlayerIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClaimedPlayerId(claimedPlayerEntity: ClaimedPlayerEntity)

    @Query(
        value = """
            DELETE FROM claimed_players
            WHERE playerId = :playerId
        """
    )
    suspend fun deleteClaimedPlayerId(playerId: String)
}

class FakeClaimedPlayerDao : ClaimedPlayerDao {
    override fun getClaimedPlayerIds(): Flow<List<String>> {
        return flowOf(listOf("test-player-id"))
    }

    override suspend fun insertClaimedPlayerId(claimedPlayerEntity: ClaimedPlayerEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteClaimedPlayerId(playerId: String) {
        TODO("Not yet implemented")
    }
}