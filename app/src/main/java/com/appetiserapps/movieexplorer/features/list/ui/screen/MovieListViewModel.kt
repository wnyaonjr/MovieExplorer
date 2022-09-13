package com.appetiserapps.movieexplorer.features.list.ui.screen

import androidx.lifecycle.*
import com.appetiserapps.movieexplorer.features.list.domain.Movie
import com.appetiserapps.movieexplorer.features.list.framework.MovieListUseCases
import com.appetiserapps.movieexplorer.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListUseCases: MovieListUseCases
) : ViewModel() {

    val trackName = MutableLiveData<String>()
    private val movieCount = movieListUseCases.getMovieCountUseCase().asLiveData()

    private val _movies = MediatorLiveData<List<Movie>?>()
    val movies: LiveData<List<Movie>?> = _movies

    private val _navigateToMovieDetails = SingleLiveEvent<Int>()
    val navigateToMovieDetails: LiveData<Int> = _navigateToMovieDetails

    private var delayedSearchJob: Job? = null

    init {
        initSources()
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
            _movies.value = movieListUseCases.sortMoviesUseCase(
                movies = movieListUseCases.getMoviesUseCase(null).firstOrNull()
            )
        }
    }

    fun onSearch(query: String) {
        Timber.d("onSearch --> query: $query")
        trackName.value = query

        delayedSearchJob?.cancel()
        delayedSearchJob = viewModelScope.launch {
            delay(TEXT_CHANGE_DEBOUNCE)
            movieListUseCases.updateMoviesUseCase(query).onEach {
                //TODO display of loading, success, and error message
            }.launchIn(viewModelScope)
        }
    }

    fun onFavoriteClick(trackId: Int, favorite: Boolean) {
        viewModelScope.launch {
            movieListUseCases.favoriteMovieUseCase(trackId, favorite)
        }
    }

    fun onMovieClick(trackId: Int) {
        _navigateToMovieDetails.value = trackId
    }

    companion object {
        private const val TEXT_CHANGE_DEBOUNCE = 1000L
    }


}