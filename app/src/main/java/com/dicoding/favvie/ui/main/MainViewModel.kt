package com.dicoding.favvie.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.core.data.remote.response.MovieResponse
import com.dicoding.core.data.remote.response.ResultsItem
import com.dicoding.core.data.remote.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listMovie = MutableLiveData<List<ResultsItem>>()
    val listMovie: LiveData<List<ResultsItem>> = _listMovie

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private var searchQuery: String? = null

    fun searchMovie(query: String?) {
        searchQuery = query
        findMovie()
    }

    init {
        findMovie()
    }

    private fun findMovie() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchMovie(searchQuery ?: DEF_SEARCH)
        client.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listMovie.value = responseBody.results
                    } else {
                        Log.e("MainViewModel", "Response body is null")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Log.e("MainViewModel", "Response error: $errorMessage")
                    _toastMessage.value = "Response error: $errorMessage"
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "Failed to fetch movie data", t)
                _toastMessage.value = "Failed to fetch movie data"
            }
        })
    }

    companion object {
        private const val DEF_SEARCH = "a"
    }
}