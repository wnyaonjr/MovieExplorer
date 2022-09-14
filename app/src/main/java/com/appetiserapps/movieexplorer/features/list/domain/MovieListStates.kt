package com.appetiserapps.movieexplorer.features.list.domain

/**
 * Wrapper class for data used in movie list screen
 */
data class MovieListStates(
    val movies: List<Movie>?,
    val trackName: String?,
    val currentState: MovieListState?
)