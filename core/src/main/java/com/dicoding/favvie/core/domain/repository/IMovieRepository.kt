package com.dicoding.favvie.core.domain.repository

import com.dicoding.favvie.core.data.Resource
import com.dicoding.favvie.core.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun getAllMovies() : Flow<Resource<List<Movie>>>

    fun getFavoriteMovie() : Flow<List<Movie>>

    fun setFavoriteMovie(movie: Movie, state: Boolean)

    fun searchMovie(query: String) : Flow<List<Movie>>
}