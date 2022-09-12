package com.appetiserapps.movieexplorer.features.list.framework

import com.appetiserapps.movieexplorer.repository.MovieRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    operator fun invoke(
        trackName: String?
    ) = movieRepository.getMovies(trackName)
}