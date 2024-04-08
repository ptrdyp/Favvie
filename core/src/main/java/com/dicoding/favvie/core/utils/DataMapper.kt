package com.dicoding.favvie.core.utils

import com.dicoding.favvie.core.data.local.entity.MovieEntity
import com.dicoding.favvie.core.data.remote.response.ResultsItem
import com.dicoding.favvie.core.domain.model.Movie

object DataMapper {
    fun mapMovieResponsesToEntities(input: List<ResultsItem>): List<MovieEntity> {
        val movieList = ArrayList<MovieEntity>()

        input.map {
            val movie = MovieEntity(
                id = it.id,
                overview = it.overview,
                title = it.title,
                posterPath = it.posterPath ?: "",
                voteAverage = it.voteAverage,
                voteCount = it.voteCount,
                isFavorite = false
            )

            movieList.add(movie)
        }

        return movieList
    }

    fun mapMovieEntitiesToDomain(input: List<MovieEntity>): List<Movie> =
        input.map {
            Movie(
                id = it.id,
                overview = it.overview,
                title = it.title,
                posterPath = it.posterPath,
                voteAverage = it.voteAverage,
                voteCount = it.voteCount,
                isFavorite = it.isFavorite
            )
        }

    fun mapMovieDomainToEntities(input: Movie) = MovieEntity (
        id = input.id,
        overview = input.overview,
        title = input.title,
        posterPath = input.posterPath,
        voteAverage = input.voteAverage,
        voteCount = input.voteCount,
        isFavorite = input.isFavorite
    )

}