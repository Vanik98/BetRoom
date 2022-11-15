package com.vanik.growdb.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vanik.growdb.model.Actor
import com.vanik.growdb.model.Movie
import com.vanik.growdb.room.dao.ActorDao
import com.vanik.growdb.room.dao.MovieDao

@Database(entities = [Actor::class, Movie::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ActorDao(): ActorDao
    abstract fun MovieDao(): MovieDao
}

