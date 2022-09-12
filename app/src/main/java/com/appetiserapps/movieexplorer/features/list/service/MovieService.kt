package com.appetiserapps.movieexplorer.features.list.service

import com.appetiserapps.movieexplorer.features.list.network.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("search")
    suspend fun getMovies(
        @Query("term") term: String,
        @Query("country") country: String,
    ): MovieListResponse
}