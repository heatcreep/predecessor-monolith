package com.aowen.monolith.`data`.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.aowen.monolith.`data`.database.dao.ClaimedPlayerDao
import com.aowen.monolith.`data`.database.dao.ClaimedPlayerDao_Impl
import com.aowen.monolith.`data`.database.dao.FavoriteBuildDao
import com.aowen.monolith.`data`.database.dao.FavoriteBuildDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class MonolithDatabase_Impl : MonolithDatabase() {
  private val _favoriteBuildDao: Lazy<FavoriteBuildDao> = lazy {
    FavoriteBuildDao_Impl(this)
  }

  private val _claimedPlayerDao: Lazy<ClaimedPlayerDao> = lazy {
    ClaimedPlayerDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "19864c856dcd6fb440126322f09d37f1", "2ad418bb0ce5fbea4f29047bd8e3572d") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `favorite_builds` (`buildId` INTEGER NOT NULL, `heroId` INTEGER NOT NULL, `role` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT DEFAULT '', `author` TEXT NOT NULL, `crestId` INTEGER NOT NULL, `itemIds` TEXT NOT NULL, `upvotesCount` INTEGER NOT NULL, `downvotesCount` INTEGER NOT NULL, `createdAt` TEXT DEFAULT '', `gameVersion` TEXT NOT NULL, PRIMARY KEY(`buildId`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `claimed_players` (`playerId` TEXT NOT NULL, PRIMARY KEY(`playerId`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '19864c856dcd6fb440126322f09d37f1')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `favorite_builds`")
        connection.execSQL("DROP TABLE IF EXISTS `claimed_players`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsFavoriteBuilds: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFavoriteBuilds.put("buildId", TableInfo.Column("buildId", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFavoriteBuilds.put("heroId", TableInfo.Column("heroId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFavoriteBuilds.put("role", TableInfo.Column("role", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFavoriteBuilds.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFavoriteBuilds.put("description", TableInfo.Column("description", "TEXT", false, 0,
            "''", TableInfo.CREATED_FROM_ENTITY))
        _columnsFavoriteBuilds.put("author", TableInfo.Column("author", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFavoriteBuilds.put("crestId", TableInfo.Column("crestId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFavoriteBuilds.put("itemIds", TableInfo.Column("itemIds", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFavoriteBuilds.put("upvotesCount", TableInfo.Column("upvotesCount", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFavoriteBuilds.put("downvotesCount", TableInfo.Column("downvotesCount", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFavoriteBuilds.put("createdAt", TableInfo.Column("createdAt", "TEXT", false, 0,
            "''", TableInfo.CREATED_FROM_ENTITY))
        _columnsFavoriteBuilds.put("gameVersion", TableInfo.Column("gameVersion", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFavoriteBuilds: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesFavoriteBuilds: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoFavoriteBuilds: TableInfo = TableInfo("favorite_builds", _columnsFavoriteBuilds,
            _foreignKeysFavoriteBuilds, _indicesFavoriteBuilds)
        val _existingFavoriteBuilds: TableInfo = read(connection, "favorite_builds")
        if (!_infoFavoriteBuilds.equals(_existingFavoriteBuilds)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |favorite_builds(com.aowen.monolith.data.database.model.FavoriteBuildListEntity).
              | Expected:
              |""".trimMargin() + _infoFavoriteBuilds + """
              |
              | Found:
              |""".trimMargin() + _existingFavoriteBuilds)
        }
        val _columnsClaimedPlayers: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsClaimedPlayers.put("playerId", TableInfo.Column("playerId", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysClaimedPlayers: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesClaimedPlayers: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoClaimedPlayers: TableInfo = TableInfo("claimed_players", _columnsClaimedPlayers,
            _foreignKeysClaimedPlayers, _indicesClaimedPlayers)
        val _existingClaimedPlayers: TableInfo = read(connection, "claimed_players")
        if (!_infoClaimedPlayers.equals(_existingClaimedPlayers)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |claimed_players(com.aowen.monolith.data.database.model.ClaimedPlayerEntity).
              | Expected:
              |""".trimMargin() + _infoClaimedPlayers + """
              |
              | Found:
              |""".trimMargin() + _existingClaimedPlayers)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "favorite_builds",
        "claimed_players")
  }

  public override fun clearAllTables() {
    super.performClear(false, "favorite_builds", "claimed_players")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(FavoriteBuildDao::class, FavoriteBuildDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ClaimedPlayerDao::class, ClaimedPlayerDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun favoriteBuildListItemDao(): FavoriteBuildDao = _favoriteBuildDao.value

  public override fun claimedPlayerDao(): ClaimedPlayerDao = _claimedPlayerDao.value
}
