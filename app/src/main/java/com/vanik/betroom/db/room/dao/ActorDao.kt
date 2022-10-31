package com.vanik.betroom.db.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vanik.betroom.entity.Actor

@Dao
interface ActorDao {
    @Query("SELECT * FROM Actor")
    suspend fun getAllActors(): List<Actor>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(actor: Actor)

    @Update
    suspend fun update(actor: Actor)
}