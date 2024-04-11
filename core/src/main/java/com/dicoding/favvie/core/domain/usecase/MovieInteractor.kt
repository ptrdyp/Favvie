package com.dicoding.favvie.core.domain.usecase

import com.dicoding.favvie.core.domain.model.Movie
import com.dicoding.favvie.core.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow

class MovieInteractor(private val movieRepository: IMovieRepository) : MovieUseCase {
    override fun getAllMovie() = movieRepository.getAllMovies()

    override fun getFavoriteMovie() = movieRepository.getFavoriteMovie()
    override fun setFavoriteMovie(movie: Movie, state: Boolean) = movieRepository.setFavoriteMovie(movie, state)
    override fun searchMovie(query: String): Flow<List<Movie>> {
        return movieRepository.searchMovie(query)
    }
}