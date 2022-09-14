package com.appetiserapps.movieexplorer.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.appetiserapps.movieexplorer.local.db.dao.MovieDao
import com.appetiserapps.movieexplorer.local.db.entity.MovieEntity

/**
 * Database definition which contains movie entity and dao
 */
@Database(
    entities = [
        MovieEntity::class
    ],
    version = 1,
)
abstract class Database : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}