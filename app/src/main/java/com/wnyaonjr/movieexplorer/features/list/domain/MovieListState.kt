package com.wnyaonjr.movieexplorer.features.list.domain

/**
 * Class for different states of movies list
 * DEFAULT - displaying favorite movies when there is no search input
 * LOADING - during network request when searching movies
 * ERROR - any error encountered during network requests
 */
enum class MovieListState {
    DEFAULT,
    LOADING,
    ERROR
}