package com.dicoding.favvie.di

import com.dicoding.favvie.core.domain.usecase.MovieInteractor
import com.dicoding.favvie.core.domain.usecase.MovieUseCase
import com.dicoding.favvie.main.MainViewModel
import com.dicoding.favvie.detail.DetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val useCaseModule = module {
    factory<MovieUseCase> { MovieInteractor(get()) }
}

val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::DetailViewModel)
}