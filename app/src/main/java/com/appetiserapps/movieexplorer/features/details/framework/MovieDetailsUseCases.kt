package com.appetiserapps.movieexplorer.features.details.framework

import com.appetiserapps.movieexplorer.features.list.framework.FavoriteMovieUseCase
import javax.inject.Inject

/**
 * Contains the use cases for movie details screen
 */
class MovieDetailsUseCases @Inject constructor(
    val getMovieUseCase: GetMovieUseCase,
    val favoriteMovieUseCase: FavoriteMovieUseCase,
)