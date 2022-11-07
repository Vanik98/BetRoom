package com.vanik.betroom.modules.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vanik.betroom.proxy.model.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM Movie")
    suspend fun getAllMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)
}