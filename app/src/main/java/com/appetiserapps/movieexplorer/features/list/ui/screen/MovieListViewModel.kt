package com.appetiserapps.movieexplorer.features.list.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appetiserapps.movieexplorer.features.list.framework.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    init {
        searchMovies()
    }

    fun searchMovies() {
        getMoviesUseCase("star").onEach { wrapper ->
            Timber.d("getMoviesUseCase -> wrapper: $wrapper")
        }.launchIn(viewModelScope)
    }
}