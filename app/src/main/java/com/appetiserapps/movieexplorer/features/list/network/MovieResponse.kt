package com.appetiserapps.movieexplorer.features.list.network


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieResponse(
    @Json(name = "trackId")
    val trackId: Int,
    @Json(name = "trackName")
    val trackName: String,
    @Json(name = "artworkUrl100")
    val artworkUrl100: String,
    @Json(name = "trackPrice")
    val trackPrice: Double,
    @Json(name = "longDescription")
    val longDescription: String?,
    @Json(name = "primaryGenreName")
    val primaryGenreName: String
)