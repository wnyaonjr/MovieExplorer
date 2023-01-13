package com.wnyaonjr.movieexplorer.features.list.framework

import com.wnyaonjr.movieexplorer.features.list.domain.Movie
import javax.inject.Inject

/**
 * Use case to sort movies alphabetically by track name and based on favorites status
 */
class SortMoviesUseCase @Inject constructor() {
    operator fun invoke(movies: List<Movie>?) =
        movies?.sortedBy { it.trackName }?.sortedBy { !it.favorite }
}