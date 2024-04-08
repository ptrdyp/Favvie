package com.dicoding.favvie.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.favvie.core.domain.usecase.MovieUseCase

class FavoriteViewModel(movieUseCase: MovieUseCase): ViewModel() {
    val favoriteMovie = movieUseCase.getFavoriteMovie().asLiveData()
}