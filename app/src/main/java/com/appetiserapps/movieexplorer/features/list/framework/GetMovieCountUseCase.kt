package com.appetiserapps.movieexplorer.features.list.framework

import com.appetiserapps.movieexplorer.repository.MovieRepository
import javax.inject.Inject

class GetMovieCountUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    operator fun invoke() = movieRepository.getMovieCount()
}