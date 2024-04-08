package com.dicoding.favvie.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.favvie.core.data.local.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}