package com.dicoding.favvie.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.favvie.core.domain.model.Movie
import com.dicoding.favvie.core.domain.usecase.MovieUseCase

class MainViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    val movie = movieUseCase.getAllMovie().asLiveData()

    fun searchMovie(query: String): LiveData<List<Movie>> {
        Log.d("MainViewModel", "searchMovie called with query: $query")
        return movieUseCase.searchMovie(query).asLiveData()
    }
}