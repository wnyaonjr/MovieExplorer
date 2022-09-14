package com.appetiserapps.movieexplorer.features.list.ui.screen

import androidx.lifecycle.*
import com.appetiserapps.movieexplorer.features.list.domain.Movie
import com.appetiserapps.movieexplorer.features.list.framework.MovieListUseCases
import com.appetiserapps.movieexplorer.features.list.network.MovieListResponse
import com.appetiserapps.movieexplorer.network.ResultWrapper
import com.appetiserapps.movieexplorer.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private val _movies = MediatorLiveData<List<Movie>?>()
    val movies: LiveData<List<Movie>?> = _movies

    private val _navigateToMovieDetails = SingleLiveEvent<Int>()
    val navigateToMovieDetails: LiveData<Int> = _navigateToMovieDetails

    private var delayedSearchJob: Job? = null
    private var searchJob: Job? = null

    private val movieSearchResults = MutableLiveData<List<Int>?>()

    init {
        initSources()
        refreshMovieList()
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
        _movies.addSource(movieSearchResults) {
            refreshMovieList()
        }
    }

    private fun refreshMovieList() {
        viewModelScope.launch {

            val moviesList = movieSearchResults.value

            _movies.value = movieListUseCases.sortMoviesUseCase(
                movies = movieListUseCases.getMoviesUseCase(null).firstOrNull()
                    ?.filter { moviesList?.contains(it.trackId) ?: it.favorite }
            )
        }
    }

    fun onTextChange(query: String) {
        trackName.value = query

        cancelOngoingJobs()

        if (query.isNotBlank()) {
            delayedSearchJob = viewModelScope.launch {
                delay(TEXT_CHANGE_DEBOUNCE)
                onSearch(query)
            }
        } else {
            movieSearchResults.value = null
        }
    }

    private fun cancelOngoingJobs() {
        delayedSearchJob?.cancel()
        searchJob?.cancel()
    }

    private fun onSearch(query: String) {
        searchJob = movieListUseCases.updateMoviesUseCase(query).onEach { wrapper ->
            //TODO display of loading, success, and error message

            if (wrapper is ResultWrapper.Success) {
                handleSuccessUpdateMovies(wrapper.value)
            }
        }.launchIn(viewModelScope)
    }

    private fun handleSuccessUpdateMovies(response: MovieListResponse) {
        movieSearchResults.value = response.results.map { it.trackId }
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