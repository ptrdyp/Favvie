package com.dicoding.favvie.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.core.data.FavoriteRepository
import com.dicoding.core.data.local.entity.FavoriteEntity
import com.dicoding.core.data.remote.response.DetailMovieResponse
import com.dicoding.core.data.remote.network.ApiConfig
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class DetailViewModel(application: Application) : ViewModel() {

    private val _detailMovie = MutableLiveData<DetailMovieResponse>()
    val detailMovie : MutableLiveData<DetailMovieResponse> = _detailMovie

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    init {
        getDetailMovie()
    }

    fun getDetailMovie(id: Int = 0) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailMovie(id)
        client.enqueue(object : Callback<DetailMovieResponse> {
            override fun onResponse(
                call: Call<DetailMovieResponse>,
                response: Response<DetailMovieResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _detailMovie.value = response.body()
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                    _toastMessage.value = "Gagal memuat data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<DetailMovieResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _toastMessage.value = "Gagal memuat data: ${t.message}"
            }
        })
    }

    fun getDataById(movie: Int) = favoriteRepository.getDataById(movie)

    fun insert(movie: FavoriteEntity.Item) {
        favoriteRepository.insert(movie)
    }

    fun delete(movie: FavoriteEntity.Item) {
        favoriteRepository.delete(movie)
    }
    companion object{
        private const val TAG = "DetailViewModel"
    }
}
