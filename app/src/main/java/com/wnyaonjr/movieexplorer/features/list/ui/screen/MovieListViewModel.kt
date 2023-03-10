package com.wnyaonjr.movieexplorer.features.list.ui.screen

import androidx.lifecycle.*
import com.wnyaonjr.movieexplorer.features.list.domain.Movie
import com.wnyaonjr.movieexplorer.features.list.domain.MovieListState
import com.wnyaonjr.movieexplorer.features.list.framework.MovieListUseCases
import com.wnyaonjr.movieexplorer.features.list.network.MovieListResponse
import com.wnyaonjr.movieexplorer.network.ResultWrapper
import com.wnyaonjr.movieexplorer.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Contains the main functions supported by movie list screen such as getting favorite movies, search movies from API, refresh list,
 * show error, and setting movie as favorite
 */
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

    /**
     * defines the fields to check for changes to refresh movie list
     */
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

    /**
     * refresh movie list from local database
     */
    private fun refreshMovieList() {
        viewModelScope.launch {
            _movies.value = movieListUseCases.getDisplayMoviesUseCase(
                localMovies = movieListUseCases.getMoviesUseCase().firstOrNull(),
                remoteMovies = movieSearchResults.value
            )
        }
    }

    /**
     * called during user search, adds a one-second waiting time before making the network request
     * reset states and fields when no search request to display favorite movies locally when search query is unavailable
     */
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

    /**
     * cancel job for searching and actual search
     */
    private fun cancelOngoingJobs() {
        delayedSearchJob?.cancel()
        searchJob?.cancel()
    }

    /**
     * network request for searching movies
     */
    private fun onSearch(query: String) {
        searchJob = movieListUseCases.updateMoviesUseCase(query).onEach { wrapper ->
            if (wrapper is ResultWrapper.Success) {
                handleSuccessUpdateMovies(wrapper.value)
            }
            handleResult(wrapper)
        }.launchIn(viewModelScope)
    }

    /**
     * change state of movie list based on network request result
     */
    private fun handleResult(wrapper: ResultWrapper<*>) {
        _currentState.value = when (wrapper) {
            is ResultWrapper.Error -> MovieListState.ERROR
            is ResultWrapper.Loading -> MovieListState.LOADING
            is ResultWrapper.Success -> MovieListState.DEFAULT
        }
    }

    /**
     * save id of movie search results used in proper display of movies from local data
     */
    private fun handleSuccessUpdateMovies(response: MovieListResponse) {
        movieSearchResults.value = response.results.map { it.trackId }
    }

    /**
     * handling when user change favorite status of movie
     */
    fun onFavoriteClick(trackId: Int, favorite: Boolean) {
        viewModelScope.launch {
            movieListUseCases.favoriteMovieUseCase(trackId, favorite)
        }
    }

    /**
     * handling when user click a movie item from list
     */
    fun onMovieClick(trackId: Int) {
        _navigateToMovieDetails.value = trackId
    }

    /**
     * handles pull down to refresh of list
     */
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