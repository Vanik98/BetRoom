package com.vanik.betroom.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vanik.betroom.entity.Actor
import com.vanik.betroom.entity.Movie
import com.vanik.betroom.entity.Pet
import com.vanik.betroom.db.room.dao.ActorDao
import com.vanik.betroom.db.room.dao.MovieDao

@Database(entities = [Actor::class, Movie::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ActorDao(): ActorDao
    abstract fun MovieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "database_room").build()
    }
}

