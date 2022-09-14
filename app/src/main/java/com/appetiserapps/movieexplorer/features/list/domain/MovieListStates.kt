package com.appetiserapps.movieexplorer.features.list.domain

data class MovieListStates(
    val movies: List<Movie>?,
    val trackName: String?,
    val currentState: MovieListState?
)