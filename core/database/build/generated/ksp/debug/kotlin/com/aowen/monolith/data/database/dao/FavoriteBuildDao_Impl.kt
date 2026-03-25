package com.aowen.monolith.`data`.database.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.aowen.monolith.`data`.database.model.FavoriteBuildListEntity
import com.aowen.monolith.`data`.database.util.IntListConverter
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class FavoriteBuildDao_Impl(
  __db: RoomDatabase,
) : FavoriteBuildDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfFavoriteBuildListEntity: EntityInsertAdapter<FavoriteBuildListEntity>

  private val __intListConverter: IntListConverter = IntListConverter()
  init {
    this.__db = __db
    this.__insertAdapterOfFavoriteBuildListEntity = object :
        EntityInsertAdapter<FavoriteBuildListEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR IGNORE INTO `favorite_builds` (`buildId`,`heroId`,`role`,`title`,`description`,`author`,`crestId`,`itemIds`,`upvotesCount`,`downvotesCount`,`createdAt`,`gameVersion`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FavoriteBuildListEntity) {
        statement.bindLong(1, entity.buildId.toLong())
        statement.bindLong(2, entity.heroId)
        statement.bindText(3, entity.role)
        statement.bindText(4, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpDescription)
        }
        statement.bindText(6, entity.author)
        statement.bindLong(7, entity.crestId.toLong())
        val _tmp: String = __intListConverter.intListToString(entity.itemIds)
        statement.bindText(8, _tmp)
        statement.bindLong(9, entity.upvotesCount.toLong())
        statement.bindLong(10, entity.downvotesCount.toLong())
        val _tmpCreatedAt: String? = entity.createdAt
        if (_tmpCreatedAt == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpCreatedAt)
        }
        statement.bindText(12, entity.gameVersion)
      }
    }
  }

  public override suspend
      fun insertFavoriteBuildListItem(favoriteBuildListItem: FavoriteBuildListEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfFavoriteBuildListEntity.insert(_connection, favoriteBuildListItem)
  }

  public override fun getFavoriteBuildListItems(): Flow<List<FavoriteBuildListEntity>> {
    val _sql: String = "SELECT * FROM favorite_builds"
    return createFlow(__db, true, arrayOf("favorite_builds")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfBuildId: Int = getColumnIndexOrThrow(_stmt, "buildId")
        val _columnIndexOfHeroId: Int = getColumnIndexOrThrow(_stmt, "heroId")
        val _columnIndexOfRole: Int = getColumnIndexOrThrow(_stmt, "role")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfAuthor: Int = getColumnIndexOrThrow(_stmt, "author")
        val _columnIndexOfCrestId: Int = getColumnIndexOrThrow(_stmt, "crestId")
        val _columnIndexOfItemIds: Int = getColumnIndexOrThrow(_stmt, "itemIds")
        val _columnIndexOfUpvotesCount: Int = getColumnIndexOrThrow(_stmt, "upvotesCount")
        val _columnIndexOfDownvotesCount: Int = getColumnIndexOrThrow(_stmt, "downvotesCount")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfGameVersion: Int = getColumnIndexOrThrow(_stmt, "gameVersion")
        val _result: MutableList<FavoriteBuildListEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FavoriteBuildListEntity
          val _tmpBuildId: Int
          _tmpBuildId = _stmt.getLong(_columnIndexOfBuildId).toInt()
          val _tmpHeroId: Long
          _tmpHeroId = _stmt.getLong(_columnIndexOfHeroId)
          val _tmpRole: String
          _tmpRole = _stmt.getText(_columnIndexOfRole)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpAuthor: String
          _tmpAuthor = _stmt.getText(_columnIndexOfAuthor)
          val _tmpCrestId: Int
          _tmpCrestId = _stmt.getLong(_columnIndexOfCrestId).toInt()
          val _tmpItemIds: List<Int>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfItemIds)
          _tmpItemIds = __intListConverter.stringToIntList(_tmp)
          val _tmpUpvotesCount: Int
          _tmpUpvotesCount = _stmt.getLong(_columnIndexOfUpvotesCount).toInt()
          val _tmpDownvotesCount: Int
          _tmpDownvotesCount = _stmt.getLong(_columnIndexOfDownvotesCount).toInt()
          val _tmpCreatedAt: String?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmpCreatedAt = null
          } else {
            _tmpCreatedAt = _stmt.getText(_columnIndexOfCreatedAt)
          }
          val _tmpGameVersion: String
          _tmpGameVersion = _stmt.getText(_columnIndexOfGameVersion)
          _item =
              FavoriteBuildListEntity(_tmpBuildId,_tmpHeroId,_tmpRole,_tmpTitle,_tmpDescription,_tmpAuthor,_tmpCrestId,_tmpItemIds,_tmpUpvotesCount,_tmpDownvotesCount,_tmpCreatedAt,_tmpGameVersion)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteFavoriteBuildListItems(buildIds: List<Int>) {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("""
        |
        |""".trimMargin())
    _stringBuilder.append("            DELETE FROM favorite_builds")
    _stringBuilder.append("""
        |
        |""".trimMargin())
    _stringBuilder.append("            WHERE buildId in (")
    val _inputSize: Int = buildIds.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    _stringBuilder.append("""
        |
        |""".trimMargin())
    _stringBuilder.append("        ")
    val _sql: String = _stringBuilder.toString()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        for (_item: Int in buildIds) {
          _stmt.bindLong(_argIndex, _item.toLong())
          _argIndex++
        }
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllFavoriteBuildListItems() {
    val _sql: String = """
        |
        |            DELETE FROM favorite_builds
        |        
        """.trimMargin()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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
