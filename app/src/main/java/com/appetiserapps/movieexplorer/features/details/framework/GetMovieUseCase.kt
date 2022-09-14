package com.appetiserapps.movieexplorer.features.details.framework

import androidx.lifecycle.SavedStateHandle
import com.appetiserapps.movieexplorer.repository.MovieRepository
import javax.inject.Inject

/**
 * Use case to get movie from local database using track id
 * Use case to track id of movie from arguments through SavedStateHandle
 */
class GetMovieUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(trackId: Int) = movieRepository.getMovie(trackId)

    operator fun invoke(savedStateHandle: SavedStateHandle): Int? = savedStateHandle[KEY_TRACK_ID]

    companion object {
        private const val KEY_TRACK_ID = "trackId"
    }
}