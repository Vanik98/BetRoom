package com.vanik.betroom.modules.di

import androidx.room.Room
import com.vanik.betroom.modules.AddActorUseCase
import com.vanik.betroom.modules.AddMovieUseCase
import com.vanik.betroom.modules.GetActorUseCase
import com.vanik.betroom.modules.GetMovieUseCase
import com.vanik.betroom.modules.repository.Repository
import com.vanik.betroom.modules.room.AppDatabase
import com.vanik.betroom.modules.sqllite.DBHelper
import com.vanik.betroom.ui.main.MainViewModel
import com.vanik.betroom.ui.movie.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val appModules by lazy {
    listOf(
        viewModelModule,
        movieUseCaseModule,
        actorUseCaseModule,
        repositoryModule,
        roomModule,
        sqlLiteModule
    )
}

val viewModelModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { MovieViewModel(get(), get()) }
}

val actorUseCaseModule = module {
    single { AddActorUseCase(get()) }
    single { GetActorUseCase(get()) }
}

val movieUseCaseModule = module {
    single { AddMovieUseCase(get()) }
    single { GetMovieUseCase(get()) }
}

val repositoryModule = module { single { Repository(get(), get(), get()) } }

val roomModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "database_room").build() }
    single { get<AppDatabase>().ActorDao() }
    single { get<AppDatabase>().MovieDao() }
}

val sqlLiteModule = module { single { DBHelper(get()) } }



