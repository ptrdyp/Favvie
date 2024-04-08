package com.dicoding.favvie.core.domain.usecase

import com.dicoding.favvie.core.data.Resource
import com.dicoding.favvie.core.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
    fun getAllMovie() : Flow<Resource<List<Movie>>>
    fun getFavoriteMovie(): Flow<List<Movie>>
    fun setFavoriteMovie(movie: Movie, state: Boolean)

    fun searchMovie(query: String): Flow<List<Movie>>
}