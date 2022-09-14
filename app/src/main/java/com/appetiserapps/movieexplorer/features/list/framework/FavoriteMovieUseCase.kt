package com.appetiserapps.movieexplorer.features.list.framework

import com.appetiserapps.movieexplorer.repository.MovieRepository
import javax.inject.Inject

/**
 * Use case to set movie as favorite or not in local database
 */
class FavoriteMovieUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(
        trackId: Int,
        favorite: Boolean
    ) {
        movieRepository.favorite(trackId, favorite)
    }
}