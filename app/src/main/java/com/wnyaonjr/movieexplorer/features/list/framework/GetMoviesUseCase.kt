package com.wnyaonjr.movieexplorer.features.list.framework

import com.wnyaonjr.movieexplorer.repository.MovieRepository
import javax.inject.Inject

/**
 * Use case for getting movies saved locally
 */
class GetMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    operator fun invoke() = movieRepository.getMovies()
}