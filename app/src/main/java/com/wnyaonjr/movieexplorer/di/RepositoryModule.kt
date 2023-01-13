package com.wnyaonjr.movieexplorer.di

import com.wnyaonjr.movieexplorer.features.list.service.MovieService
import com.wnyaonjr.movieexplorer.local.db.Database
import com.wnyaonjr.movieexplorer.repository.MovieRepository
import com.wnyaonjr.movieexplorer.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Definition of repository dependency for network requests and local database operation
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideMovieRepository(
        movieService: MovieService,
        database: Database
    ): MovieRepository = MovieRepositoryImpl(
        movieService = movieService,
        database = database
    )
}