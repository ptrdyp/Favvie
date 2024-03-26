package com.dicoding.favvie.core.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

data class FavoriteEntity(
    val items: MutableList<Item>
) {
    @Parcelize
    @Entity(tableName = "favorite_movie")
    data class Item (
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "id")
        var id: Int,

        @ColumnInfo(name = "title")
        var title: String?,

        @ColumnInfo(name = "overview")
        var overview: String?,

        @ColumnInfo(name = "vote_average")
        val voteAverage: Float? = null,

        @ColumnInfo(name = "posterPath")
        var posterPath: String?,
    ) : Parcelable
}