package com.vanik.betroom.data.modules.room.dao

import androidx.room.*
import com.vanik.betroom.data.model.Actor

@Dao
interface ActorDao {
    @Query("SELECT * FROM Actor")
    suspend fun getAllActors(): List<Actor>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(actor: Actor)

    @Update
    suspend fun update(actor: Actor)
}