package com.appetiserapps.movieexplorer.di

import com.appetiserapps.movieexplorer.features.list.service.MovieService
import com.appetiserapps.movieexplorer.repository.MovieRepository
import com.appetiserapps.movieexplorer.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideMovieRepository(
        movieService: MovieService
    ): MovieRepository = MovieRepositoryImpl(
        movieService = movieService
    )
}