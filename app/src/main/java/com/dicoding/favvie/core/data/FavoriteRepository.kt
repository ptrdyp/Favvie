package com.dicoding.favvie.core.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.favvie.core.data.local.entity.FavoriteEntity
import com.dicoding.favvie.core.data.local.room.FavoriteDao
import com.dicoding.favvie.core.data.local.room.FavoriteDatabase
import com.dicoding.favvie.core.data.remote.response.ResultsItem
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository (application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteEntity.Item>> = mFavoriteDao.getAllFavorites()

    fun getDataById(id: Int): LiveData<List<ResultsItem>> = mFavoriteDao.getDataById(id)

    fun insert(id: FavoriteEntity.Item) {
        executorService.execute {
            mFavoriteDao.insertMovies(id)
        }
    }

    fun delete(id: FavoriteEntity.Item) {
        executorService.execute {
            mFavoriteDao.delete(id)
        }
    }
}