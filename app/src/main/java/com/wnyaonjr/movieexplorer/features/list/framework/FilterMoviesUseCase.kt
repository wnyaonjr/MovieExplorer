package com.wnyaonjr.movieexplorer.features.list.framework

import com.wnyaonjr.movieexplorer.features.list.domain.Movie
import javax.inject.Inject

/**
 * Use case to filter movies based response from search results
 * Filter local favorite movies only when movies from search result is not available
 * Filter local movies that is found in the search results
 */
class FilterMoviesUseCase @Inject constructor() {
    operator fun invoke(localMovies: List<Movie>?, remoteMovies: List<Int>?) =
        localMovies?.filter { remoteMovies?.contains(it.trackId) ?: it.favorite }
}