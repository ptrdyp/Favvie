package com.dicoding.favvie.core.data.remote.network

import com.dicoding.favvie.core.data.remote.response.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("discover/movie")
    suspend fun getMovie(): MovieResponse

    @GET("search/movie")
    fun searchMovie(
        @Query("query") query: String
    ): Call<MovieResponse>
}