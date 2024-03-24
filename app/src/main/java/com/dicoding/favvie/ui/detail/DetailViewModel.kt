package com.dicoding.favvie.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.favvie.data.response.DetailMovieResponse
import com.dicoding.favvie.data.retrofit.ApiConfig
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

    class ViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
        override fun <view : ViewModel> create(modelClass: Class<view>): view {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel(application) as view
            }
            throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
        }

        companion object {
            private var INSTANCE: ViewModelFactory? = null
            fun getInstance(application: Application): ViewModelFactory {
                if (INSTANCE == null) {
                    synchronized(ViewModelFactory::class.java) {
                        INSTANCE = ViewModelFactory(application)
                    }
                }
                return INSTANCE as ViewModelFactory
            }
        }
    }
    companion object{
        private const val TAG = "DetailViewModel"
    }
}
