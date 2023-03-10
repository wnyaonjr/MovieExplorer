package com.wnyaonjr.movieexplorer.features.list.network


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Class representation of expected JSON response from movie search result
 */
@JsonClass(generateAdapter = true)
data class MovieListResponse(
    @Json(name = "resultCount")
    val resultCount: Int,
    @Json(name = "results")
    val results: List<MovieResponse>
)