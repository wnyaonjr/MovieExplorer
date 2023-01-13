package com.wnyaonjr.movieexplorer.repository

import com.wnyaonjr.movieexplorer.features.list.domain.Movie
import com.wnyaonjr.movieexplorer.features.list.network.MovieListResponse
import com.wnyaonjr.movieexplorer.features.list.network.MovieResponse
import com.wnyaonjr.movieexplorer.features.list.service.MovieService
import com.wnyaonjr.movieexplorer.local.db.Database
import com.wnyaonjr.movieexplorer.local.db.entity.toDomainModel
import com.wnyaonjr.movieexplorer.local.db.entity.toEntityModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Definition of functions supported for remote and local data handling
 */
interface MovieRepository {
    suspend fun getMovies(term: String, country: String, media: String): MovieListResponse
    suspend fun saveMovies(movies: List<MovieResponse>)
    fun getMovies(): Flow<List<Movie>>
    fun getMovieCount(): Flow<Int>
    suspend fun favorite(trackId: Int, favorite: Boolean)
    suspend fun getMovie(trackId: Int): Movie
}

class MovieRepositoryImpl(
    private val movieService: MovieService,
    private val database: Database
) : MovieRepository {

    private val movieDao by lazy {
        database.movieDao()
    }


    override suspend fun getMovies(term: String, country: String, media: String) =
        movieService.getMovies(
            term = term,
            country = country,
            media = media
        )

    override fun getMovies() = movieDao.getAll().map {
        it.toDomainModel()
    }

    override suspend fun saveMovies(movies: List<MovieResponse>) {
        withContext(Dispatchers.IO) {
            movieDao.deleteMovies()
            movieDao.insertAll(*movies.toEntityModel().toTypedArray())
        }
    }

    override fun getMovieCount() = movieDao.getRowCount()
    override suspend fun favorite(trackId: Int, favorite: Boolean) {
        withContext(Dispatchers.IO) {
            movieDao.favorite(trackId, favorite)
        }
    }

    override suspend fun getMovie(trackId: Int) = movieDao.get(trackId).toDomainModel()

}