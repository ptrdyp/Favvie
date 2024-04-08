package com.dicoding.favvie.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie (
    val overview: String = "",
    val title: String,
    val posterPath: String = "",
    val voteAverage: Float = 0.0f,
    val id: Int,
    val voteCount: Int = 0,
    val isFavorite: Boolean = false
): Parcelable