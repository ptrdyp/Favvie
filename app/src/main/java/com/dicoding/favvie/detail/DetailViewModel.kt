package com.dicoding.favvie.detail

import androidx.lifecycle.ViewModel
import com.dicoding.favvie.core.domain.model.Movie
import com.dicoding.favvie.core.domain.usecase.MovieUseCase

class DetailViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    fun setFavoriteMovie(movie: Movie, newStatus: Boolean) =
        movieUseCase.setFavoriteMovie(movie, newStatus)
}
