package com.appetiserapps.movieexplorer.features.list.ui.screen

import androidx.lifecycle.*
import com.appetiserapps.movieexplorer.features.list.domain.Movie
import com.appetiserapps.movieexplorer.features.list.domain.MovieListState
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

    private val _currentState = MutableLiveData(MovieListState.DEFAULT)
    val currentState: LiveData<MovieListState> = _currentState

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

            _movies.value = movieListUseCases.displayMoviesUseCase(
                localMovies = movieListUseCases.getMoviesUseCase(null).firstOrNull(),
                remoteMovies = movieSearchResults.value
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
            _currentState.value = MovieListState.DEFAULT
            movieSearchResults.value = null
        }
    }

    private fun cancelOngoingJobs() {
        delayedSearchJob?.cancel()
        searchJob?.cancel()
    }

    private fun onSearch(query: String) {
        searchJob = movieListUseCases.updateMoviesUseCase(query).onEach { wrapper ->
            if (wrapper is ResultWrapper.Success) {
                handleSuccessUpdateMovies(wrapper.value)
            }
            handleResult(wrapper)
        }.launchIn(viewModelScope)
    }

    private fun handleResult(wrapper: ResultWrapper<*>) {
        _currentState.value = when (wrapper) {
            is ResultWrapper.Error -> MovieListState.ERROR
            is ResultWrapper.Loading -> MovieListState.LOADING
            is ResultWrapper.Success -> MovieListState.DEFAULT
        }
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

    fun onRefresh() {
        val currentSearch = trackName.value
        if (!currentSearch.isNullOrBlank()) {
            cancelOngoingJobs()
            onSearch(currentSearch)
        }
    }

    companion object {
        private const val TEXT_CHANGE_DEBOUNCE = 1000L
    }
}