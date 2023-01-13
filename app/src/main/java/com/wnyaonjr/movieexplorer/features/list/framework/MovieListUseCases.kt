package com.wnyaonjr.movieexplorer.features.list.framework

import javax.inject.Inject

/**
 * Wrapper class for use cases used in movie list screen
 */
class MovieListUseCases @Inject constructor(
    val updateMoviesUseCase: UpdateMoviesUseCase,
    val getMoviesUseCase: GetMoviesUseCase,
    val getMovieCountUseCase: GetMovieCountUseCase,
    val favoriteMovieUseCase: FavoriteMovieUseCase,
    val getDisplayMoviesUseCase: GetDisplayMoviesUseCase
)