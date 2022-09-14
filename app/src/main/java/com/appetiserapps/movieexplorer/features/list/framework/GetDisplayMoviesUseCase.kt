package com.appetiserapps.movieexplorer.features.list.framework

import com.appetiserapps.movieexplorer.features.list.domain.Movie
import javax.inject.Inject

class GetDisplayMoviesUseCase @Inject constructor(
    private val sortMoviesUseCase: SortMoviesUseCase,
    private val filterMoviesUseCase: FilterMoviesUseCase
) {
    operator fun invoke(localMovies: List<Movie>?, remoteMovies: List<Int>?) = sortMoviesUseCase(
        movies = filterMoviesUseCase(localMovies, remoteMovies)
    )
}