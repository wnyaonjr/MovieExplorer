package com.appetiserapps.movieexplorer.features.list.framework

import javax.inject.Inject

class MovieListUseCases @Inject constructor(
    val updateMoviesUseCase: UpdateMoviesUseCase,
    val getMoviesUseCase: GetMoviesUseCase,
    val getMovieCountUseCase: GetMovieCountUseCase,
    val favoriteMovieUseCase: FavoriteMovieUseCase,
    val sortMoviesUseCase: SortMoviesUseCase
)