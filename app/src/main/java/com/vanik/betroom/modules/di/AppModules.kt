package com.vanik.betroom.modules.di

import com.vanik.betroom.modules.AddActorUseCase
import com.vanik.betroom.modules.AddMovieUseCase
import com.vanik.betroom.modules.GetActorUseCase
import com.vanik.betroom.modules.GetMovieUseCase
import com.vanik.betroom.modules.repository.Repository
import com.vanik.betroom.ui.main.MainViewModel
import com.vanik.betroom.ui.movie.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val actorModule = module {
    single { AddActorUseCase(get()) }
    single { GetActorUseCase(get()) }
}

val repositoryModule = module { single { Repository(get()) } }

val mainViewModelModule = module {
    viewModel {
        MainViewModel(get(), get())
    }
}

val movieViewModelModule = module {
    viewModel {
        MovieViewModel(get(), get())
    }
}

val movieModule = module {
    single { AddMovieUseCase(get()) }
    single { GetMovieUseCase(get()) }
}