package com.appetiserapps.movieexplorer.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appetiserapps.movieexplorer.features.list.domain.Movie
import com.appetiserapps.movieexplorer.features.list.network.MovieResponse

@Entity(tableName = "table_movies")
data class MovieEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artworkUrl100: String,
    val trackPrice: Double,
    val longDescription: String?,
    val primaryGenreName: String,
    val favorite: Boolean = false
)

fun List<MovieResponse>.toEntityModel() = map {
    it.toEntityModel()
}

fun MovieResponse.toEntityModel() = MovieEntity(
    trackId = trackId,
    trackName = trackName,
    artworkUrl100 = artworkUrl100,
    trackPrice = trackPrice,
    longDescription = longDescription,
    primaryGenreName = primaryGenreName,
)

fun List<MovieEntity>.toDomainModel() = map {
    it.toDomainModel()
}

fun MovieEntity.toDomainModel() = Movie(
    trackId = trackId,
    trackName = trackName,
    artworkUrl100 = artworkUrl100,
    trackPrice = trackPrice,
    longDescription = longDescription,
    primaryGenreName = primaryGenreName,
    favorite = favorite
)