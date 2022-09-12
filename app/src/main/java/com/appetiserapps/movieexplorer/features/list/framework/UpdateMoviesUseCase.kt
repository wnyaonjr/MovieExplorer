package com.appetiserapps.movieexplorer.features.list.framework

import com.appetiserapps.movieexplorer.repository.MovieRepository
import com.appetiserapps.movieexplorer.repository.handler.DefaultFlowRequestHandler
import com.appetiserapps.movieexplorer.repository.handler.FlowRequestHandler
import javax.inject.Inject

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
    }

    companion object {
        private const val DEFAULT_COUNTRY = "au"
        private const val DEFAULT_MEDIA = "movie"
    }
}