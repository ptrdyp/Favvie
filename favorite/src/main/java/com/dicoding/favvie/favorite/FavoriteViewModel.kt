package com.dicoding.favvie.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.core.data.FavoriteRepository

class FavoriteViewModel(application: Application): ViewModel() {
    private val favoriteRepository: FavoriteRepository =
        FavoriteRepository(application)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllFavorites() = favoriteRepository.getAllFavorites()

}