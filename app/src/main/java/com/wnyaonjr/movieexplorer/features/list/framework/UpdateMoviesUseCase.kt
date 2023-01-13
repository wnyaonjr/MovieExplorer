package com.wnyaonjr.movieexplorer.features.list.framework

import com.wnyaonjr.movieexplorer.repository.MovieRepository
import com.wnyaonjr.movieexplorer.repository.handler.DefaultFlowRequestHandler
import com.wnyaonjr.movieexplorer.repository.handler.FlowRequestHandler
import javax.inject.Inject

/**
 * Use case to request movies based on search parameter and saving it to local database
 */
class UpdateMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) :
    FlowRequestHandler by DefaultFlowRequestHandler {
    operator fun invoke(
        term: String,
        country: String = DEFAULT_COUNTRY
    ) = safeApiCall {
        val moviesResponse = movieRepository.getMovies(
            term = term,
            country = country,
            media = DEFAULT_MEDIA
        )

        if (moviesResponse.results.isNotEmpty()) {
            movieRepository.saveMovies(moviesResponse.results)
        }

        moviesResponse
    }

    companion object {
        private const val DEFAULT_COUNTRY = "au"
        private const val DEFAULT_MEDIA = "movie"
    }
}