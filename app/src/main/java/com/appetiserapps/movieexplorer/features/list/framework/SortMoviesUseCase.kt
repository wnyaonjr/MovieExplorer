package com.appetiserapps.movieexplorer.features.list.framework

import com.appetiserapps.movieexplorer.features.list.domain.Movie
import javax.inject.Inject

class SortMoviesUseCase @Inject constructor() {
    operator fun invoke(movies: List<Movie>?) =
        movies?.sortedBy { it.trackName }?.sortedBy { !it.favorite }
}