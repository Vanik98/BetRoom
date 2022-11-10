package com.vanik.betroom.data.modules.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vanik.betroom.data.modules.room.dao.ActorDao
import com.vanik.betroom.data.modules.room.dao.MovieDao
import com.vanik.betroom.data.model.Actor
import com.vanik.betroom.data.model.Movie

@Database(entities = [Actor::class, Movie::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ActorDao(): ActorDao
    abstract fun MovieDao(): MovieDao
}

