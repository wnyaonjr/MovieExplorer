package com.appetiserapps.movieexplorer.features.list.domain

class MovieListEventListeners(
    val onFavoriteClickListener: ((trackId: Int, favorite: Boolean) -> Unit)? = null,
    val onMovieClickListener: ((trackId: Int) -> Unit)? = null,
    val searchListener: ((query: String) -> Unit)? = null,
    val onRefresh: (() -> Unit)? = null,
)