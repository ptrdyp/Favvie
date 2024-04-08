package com.dicoding.favvie

import android.app.Application
import com.dicoding.favvie.core.di.databaseModule
import com.dicoding.favvie.core.di.networkModule
import com.dicoding.favvie.core.di.repositoryModule
import com.dicoding.favvie.di.useCaseModule
import com.dicoding.favvie.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}