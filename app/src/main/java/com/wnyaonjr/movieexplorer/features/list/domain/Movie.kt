package com.wnyaonjr.movieexplorer.features.list.domain

/**
 * Class for holding movie information for view layer usage
 */
data class Movie(
    val trackId: Int,
    val trackName: String,
    val artworkUrl100: String,
    val trackPrice: Double,
    val longDescription: String?,
    val primaryGenreName: String,
    var favorite: Boolean
)