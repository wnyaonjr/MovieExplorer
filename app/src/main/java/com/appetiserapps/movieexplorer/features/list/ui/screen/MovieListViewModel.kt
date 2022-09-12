package com.appetiserapps.movieexplorer.features.list.ui.screen

import androidx.lifecycle.*
import com.appetiserapps.movieexplorer.features.list.domain.Movie
import com.appetiserapps.movieexplorer.features.list.framework.MovieListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListUseCases: MovieListUseCases
) : ViewModel() {

    val trackName = MutableLiveData<String>()
    private val movieCount = movieListUseCases.getMovieCountUseCase().asLiveData()

    private val _movies = MediatorLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    init {
        initSources()

        trackName.value = "star"
        updateMovies()
    }

    private fun initSources() {
        initMovieSources()
    }

    private fun initMovieSources() {
        _movies.addSource(trackName) {
            refreshMovieList()
        }
        _movies.addSource(movieCount) {
            refreshMovieList()
        }
    }

    private fun refreshMovieList() {
        viewModelScope.launch {
            _movies.value = movieListUseCases.getMoviesUseCase(trackName.value).firstOrNull()
        }
    }

    fun updateMovies() {
        movieListUseCases.updateMoviesUseCase("star").onEach {
            //TODO display of loading, success, and error message
        }.launchIn(viewModelScope)
    }


}