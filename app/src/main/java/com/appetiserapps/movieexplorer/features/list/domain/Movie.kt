package com.appetiserapps.movieexplorer.features.list.domain


data class Movie(
    val trackId: Int,
    val trackName: String,
    val artworkUrl100: String,
    val trackPrice: Double,
    val longDescription: String?,
    val primaryGenreName: String,
    val favorite: Boolean
)