package com.appetiserapps.movieexplorer.features.list.framework

import com.appetiserapps.movieexplorer.features.list.domain.Movie
import javax.inject.Inject

class FilterMoviesUseCase @Inject constructor() {
    operator fun invoke(localMovies: List<Movie>?, remoteMovies: List<Int>?) =
        localMovies?.filter { remoteMovies?.contains(it.trackId) ?: it.favorite }
}