package com.wnyaonjr.movieexplorer.features.details.framework

import androidx.lifecycle.SavedStateHandle
import com.wnyaonjr.movieexplorer.repository.MovieRepository
import javax.inject.Inject

/**
 * Use case to get movie from local database using track id
 * Use case to track id of movie from arguments through SavedStateHandle
 */
class GetMovieUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    /**
     * Get movie object from local database based on track id
     */
    suspend operator fun invoke(trackId: Int) = movieRepository.getMovie(trackId)

    /**
     * get track id from passed arguments through saved state handle
     */
    operator fun invoke(savedStateHandle: SavedStateHandle): Int? = savedStateHandle["trackId"]
}