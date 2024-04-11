package com.dicoding.favvie.core.data

import android.util.Log
import com.dicoding.favvie.core.data.local.LocalDataSource
import com.dicoding.favvie.core.data.remote.RemoteDataSource
import com.dicoding.favvie.core.data.remote.network.ApiResponse
import com.dicoding.favvie.core.data.remote.response.ResultsItem
import com.dicoding.favvie.core.domain.model.Movie
import com.dicoding.favvie.core.domain.repository.IMovieRepository
import com.dicoding.favvie.core.utils.AppExecutors
import com.dicoding.favvie.core.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IMovieRepository {
    override fun getAllMovies(): Flow<Resource<List<Movie>>> =
        object : NetworkBoundResource<List<Movie>, List<ResultsItem>>() {
            override fun loadFromDB(): Flow<List<Movie>> {
                return localDataSource.getAllMovies().map {
                    DataMapper.mapMovieEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Movie>?): Boolean = data.isNullOrEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<ResultsItem>>> =
                remoteDataSource.getAllMovies()

            override suspend fun saveCallResult(data: List<ResultsItem>) {
                val movieList = DataMapper.mapMovieResponsesToEntities(data)
                localDataSource.insertMovie(movieList)
            }
        }.asFlow()

    override fun getFavoriteMovie(): Flow<List<Movie>> {
        return localDataSource.getFavoriteMovie().map {
            DataMapper.mapMovieEntitiesToDomain(it)
        }
    }

    override fun setFavoriteMovie(movie: Movie, state: Boolean) {
        val movieEntity = DataMapper.mapMovieDomainToEntities(movie)
        appExecutors.diskIO().execute { localDataSource.setFavoriteMovie(movieEntity, state) }
    }

    override fun searchMovie(query: String): Flow<List<Movie>> =
        flow {
            Log.d("MovieRepository", "searchMovie called with query: $query")
            when (val apiService = remoteDataSource.searchMovie(query).first()) {
                is ApiResponse.Success<*> -> {
                    val searchResult = apiService.data
                    val favoriteMovies = localDataSource.getFavoriteMovie().first().associateBy { it.id }
                    val searchResultEntities = DataMapper.mapMovieResponsesToEntities(searchResult as List<ResultsItem>)
                    searchResultEntities.forEach { movie ->
                        movie.isFavorite = favoriteMovies.containsKey(movie.id)
                    }
                    Log.d("MovieRepository", "Received ${searchResult.size} movies from API")
                    localDataSource.insertMovie(searchResultEntities)
                    emit(DataMapper.mapMovieEntitiesToDomain(searchResultEntities))
                }
                is ApiResponse.Empty -> {
                    Log.d("MovieRepository", "No movies found for query: $query")
                    emit(emptyList())
                }
                is ApiResponse.Error -> {
                    Log.e("MovieRepository", "Error searching movies: ${apiService.errorMessage}")
                    error("Error has found")
                }
            }
        }.flowOn(Dispatchers.IO)
}