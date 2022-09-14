package com.appetiserapps.movieexplorer.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appetiserapps.movieexplorer.local.db.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM table_movies")
    fun getAll(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg entity: MovieEntity)

    @Query("DELETE from table_movies")
    fun delete()

    @Query("DELETE from table_movies WHERE favorite= :favorite")
    fun deleteMovies(favorite: Boolean = false)

    @Query("SELECT COUNT(trackId) FROM table_movies")
    fun getRowCount(): Flow<Int>

    @Query("UPDATE table_movies SET favorite = :favorite WHERE trackId = :trackId")
    suspend fun favorite(trackId: Int, favorite: Boolean)

    @Query("SELECT * FROM table_movies where trackId = :trackId")
    suspend fun get(trackId: Int): MovieEntity
}