package com.dicoding.favvie.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.favvie.data.local.FavoriteRepository

class FavoriteViewModel(application: Application): ViewModel() {
    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllFavorites() = favoriteRepository.getAllFavorites()

}