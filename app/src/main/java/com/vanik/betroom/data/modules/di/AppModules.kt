package com.vanik.betroom.data.modules.di

import com.vanik.betroom.data.modules.repository.Repository
import com.vanik.betroom.domain.usecase.AddActorUseCase
import com.vanik.betroom.domain.usecase.AddMovieUseCase
import com.vanik.betroom.domain.usecase.GetActorUseCase
import com.vanik.betroom.domain.usecase.GetMovieUseCase
import com.vanik.betroom.ui.main.MainViewModel
import com.vanik.betroom.ui.movie.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules by lazy {
    listOf(
        viewModelModule,
        movieUseCaseModule,
        actorUseCaseModule,
        repositoryModule,
    )
}

private val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { MovieViewModel(get(), get()) }
}

private val actorUseCaseModule = module {
    single { AddActorUseCase(get()) }
    single { GetActorUseCase(get()) }
}

private val movieUseCaseModule = module {
    single { AddMovieUseCase(get()) }
    single { GetMovieUseCase(get()) }
}

private val repositoryModule = module { single { Repository(get(), get(), get()) } }


