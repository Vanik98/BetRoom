package com.vanik.betroom.modules.di

import android.database.sqlite.SQLiteOpenHelper
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
import org.koin.dsl.module

val actorModule = module {
    single { AddActorUseCase(get()) }
    single { GetActorUseCase(get()) }
}

val repositoryModule = module {
    single { DBHelper(get()) }
    single { get<AppDatabase>().ActorDao() }
    single { get<AppDatabase>().MovieDao() }
    single { Repository(get(), get(), get()) }
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "database_room").build() }
}

val mainViewModelModule = module { viewModel { MainViewModel(get(), get()) } }

val movieViewModelModule = module { viewModel { MovieViewModel(get(), get()) } }

val movieModule = module {
    single { AddMovieUseCase(get()) }
    single { GetMovieUseCase(get()) }
}