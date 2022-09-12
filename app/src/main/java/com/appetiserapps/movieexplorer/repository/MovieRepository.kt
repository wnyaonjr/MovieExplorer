package com.appetiserapps.movieexplorer.repository

import com.appetiserapps.movieexplorer.features.list.network.MovieListResponse
import com.appetiserapps.movieexplorer.features.list.service.MovieService

interface MovieRepository {
    suspend fun getMovies(term: String, country: String, media: String): MovieListResponse
}

class MovieRepositoryImpl(
    private val movieService: MovieService
) : MovieRepository {
    override suspend fun getMovies(term: String, country: String, media: String) =
        movieService.getMovies(
            term = term,
            country = country,
            media = media
        )

}