package com.wnyaonjr.movieexplorer.features.details.ui.screen

import androidx.lifecycle.*
import com.wnyaonjr.movieexplorer.features.details.framework.MovieDetailsUseCases
import com.wnyaonjr.movieexplorer.features.list.domain.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Contains the main functions supported by movie details screen such as getting movie details, and setting movie as favorite
 */
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieDetailsUseCases: MovieDetailsUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    init {
        val trackId = movieDetailsUseCases.getMovieUseCase(savedStateHandle)
        if (trackId == null) {
            throw IllegalAccessException("trackId is required")
        } else {
            getMovie(trackId)
        }
    }

    /**
     * get movie object from local database
     */
    private fun getMovie(trackId: Int) {
        viewModelScope.launch {
            _movie.value = movieDetailsUseCases.getMovieUseCase(trackId)
        }
    }


    /**
     * handling when user change favorite status of movie
     */
    fun onFavoriteClick(trackId: Int, favorite: Boolean) {
        viewModelScope.launch {
            movieDetailsUseCases.favoriteMovieUseCase(trackId, favorite)
        }
    }
}