package com.aowen.monolith.`data`.database.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.aowen.monolith.`data`.database.model.ClaimedPlayerEntity
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ClaimedPlayerDao_Impl(
  __db: RoomDatabase,
) : ClaimedPlayerDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfClaimedPlayerEntity: EntityInsertAdapter<ClaimedPlayerEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfClaimedPlayerEntity = object : EntityInsertAdapter<ClaimedPlayerEntity>()
        {
      protected override fun createQuery(): String =
          "INSERT OR IGNORE INTO `claimed_players` (`playerId`) VALUES (?)"

      protected override fun bind(statement: SQLiteStatement, entity: ClaimedPlayerEntity) {
        statement.bindText(1, entity.playerId)
      }
    }
  }

  public override suspend fun insertClaimedPlayerId(claimedPlayerEntity: ClaimedPlayerEntity): Unit
      = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfClaimedPlayerEntity.insert(_connection, claimedPlayerEntity)
  }

  public override fun getClaimedPlayerIds(): Flow<List<String>> {
    val _sql: String = """
        |
        |            SELECT playerId FROM claimed_players
        |        
        """.trimMargin()
    return createFlow(__db, true, arrayOf("claimed_players")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: MutableList<String> = mutableListOf()
        while (_stmt.step()) {
          val _item: String
          _item = _stmt.getText(0)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteClaimedPlayerId(playerId: String) {
    val _sql: String = """
        |
        |            DELETE FROM claimed_players
        |            WHERE playerId = ?
        |        
        """.trimMargin()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, playerId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
