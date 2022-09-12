package com.appetiserapps.movieexplorer.features.list.framework

import com.appetiserapps.movieexplorer.repository.MovieRepository
import com.appetiserapps.movieexplorer.repository.handler.DefaultFlowRequestHandler
import com.appetiserapps.movieexplorer.repository.handler.FlowRequestHandler
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) :
    FlowRequestHandler by DefaultFlowRequestHandler {
    operator fun invoke(term: String, country: String = DEFAULT_COUNTRY) = safeApiCall {
        movieRepository.getMovies(
            term = term,
            country = country
        )
    }

    companion object {
        private const val DEFAULT_COUNTRY = "au"
    }
}