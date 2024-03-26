package com.dicoding.favvie.data.remote.retrofit

import com.dicoding.favvie.data.remote.response.DetailMovieResponse
import com.dicoding.favvie.data.remote.response.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/movie")
    fun searchMovie(
        @Query("query") query: String
    ): Call<MovieResponse>

    @GET("movie/{movie_id}")
    fun getDetailMovie(
        @Path("movie_id") id: Int
    ): Call<DetailMovieResponse>
}