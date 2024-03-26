package com.dicoding.favvie.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.favvie.data.local.entity.FavoriteEntity
import com.dicoding.favvie.data.remote.response.ResultsItem

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_movie ORDER BY id ASC")
    fun getAllFavorites(): LiveData<List<FavoriteEntity.Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movie: FavoriteEntity.Item)

    @Query("SELECT * FROM favorite_movie WHERE id = :id")
    fun getDataById(id: Int): LiveData<List<ResultsItem>>

    @Delete
    fun delete(id: FavoriteEntity.Item)
}