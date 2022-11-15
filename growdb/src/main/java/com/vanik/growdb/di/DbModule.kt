package com.vanik.growdb.di

import androidx.room.Room
import com.vanik.growdb.room.AppDatabase
import com.vanik.growdb.sqllite.DBHelper
import org.koin.dsl.module

val dbModules by lazy {
    listOf(
        roomModule,
        sqlLiteModule
    )
}

private val roomModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "database_room").build() }
    single { get<AppDatabase>().ActorDao() }
    single { get<AppDatabase>().MovieDao() }
}

private val sqlLiteModule = module { single { DBHelper(get()) } }


