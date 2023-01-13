package com.wnyaonjr.movieexplorer.features.list.domain

/**
 * Wrapper class for event listeners for movie list
 */
class MovieListEventListeners(
    val onFavoriteClickListener: ((trackId: Int, favorite: Boolean) -> Unit)? = null,
    val onMovieClickListener: ((trackId: Int) -> Unit)? = null,
    val searchListener: ((query: String) -> Unit)? = null,
    val onRefresh: (() -> Unit)? = null,
)