package com.wnyaonjr.movieexplorer.features.list.framework

import com.wnyaonjr.movieexplorer.repository.MovieRepository
import javax.inject.Inject

/**
 * Use case to get number of local data
 * Use as reference to refresh list during setting of movie as favorite
 */
class GetMovieCountUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    operator fun invoke() = movieRepository.getMovieCount()
}