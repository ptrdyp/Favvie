package com.dicoding.favvie.core.di

import androidx.room.Room
import com.dicoding.core.BuildConfig
import com.dicoding.favvie.core.data.MovieRepository
import com.dicoding.favvie.core.data.remote.RemoteDataSource
import com.dicoding.favvie.core.data.local.LocalDataSource
import com.dicoding.favvie.core.data.local.room.MovieDatabase
import com.dicoding.favvie.core.data.remote.network.ApiService
import com.dicoding.favvie.core.domain.repository.IMovieRepository
import com.dicoding.favvie.core.utils.AppExecutors
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<MovieDatabase>().movieDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("alviany".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            MovieDatabase::class.java, "Movies.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val hostname = "favvie.com"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/Pz9/+q7MnMN/ZTYCJ8MucGFzdc38B0G+Y/mweg+Mq9o=")
            .add(hostname, "sha256/J2/oqMTsdhFWW/n85tys6b4yDBtb6idZayIEBx7QTxA=")
            .add(hostname, "sha256/diGVwiVYbubAI3RW4hB9xU8e/CH2GnkuvVFZE8zmgzI=")
            .build()
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val mySuperSecretKey = BuildConfig.KEY
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer $mySuperSecretKey")
                .build()
            chain.proceed(requestHeaders)
        }

        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }

        okHttpClientBuilder.build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IMovieRepository> {
        MovieRepository(
            get(),
            get(),
            get()
        )
    }
}