package com.vanik.betroom.modules.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vanik.betroom.modules.room.dao.ActorDao
import com.vanik.betroom.modules.room.dao.MovieDao
import com.vanik.betroom.proxy.model.Actor
import com.vanik.betroom.proxy.model.Movie

@Database(entities = [Actor::class, Movie::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ActorDao(): ActorDao
    abstract fun MovieDao(): MovieDao
}

