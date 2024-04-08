package com.dicoding.favvie.core.data.remote

import com.dicoding.favvie.core.data.remote.network.ApiResponse
import com.dicoding.favvie.core.data.remote.network.ApiService
import com.dicoding.favvie.core.data.remote.response.MovieResponse
import com.dicoding.favvie.core.data.remote.response.ResultsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RemoteDataSource (private val apiService: ApiService) {

    suspend fun getAllMovies(): Flow<ApiResponse<List<ResultsItem>>> {
        return flow {
            try {
                val response = apiService.getMovie()
                val dataArray = response.results
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.results))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.catch { e ->
            emit(ApiResponse.Error("Exception caught: ${e.message}"))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun searchMovie(query: String): Flow<ApiResponse<List<ResultsItem>>> {
        return flow {
            val response = apiService.searchMovie(query)
            val result = suspendCoroutine { continuation ->
                response.enqueue(object : Callback<MovieResponse> {
                    override fun onResponse(
                        call: Call<MovieResponse>,
                        response: Response<MovieResponse>
                    ) {
                        continuation.resume(response.body())
                    }

                    override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
            }
            if (result != null) {
                emit(ApiResponse.Success(result.results))
            } else {
                emit(ApiResponse.Error("Response body is null"))
            }
        }.catch { e ->
            emit(ApiResponse.Error("Exception caught: ${e.message}"))
        }.flowOn(Dispatchers.IO)
    }
}