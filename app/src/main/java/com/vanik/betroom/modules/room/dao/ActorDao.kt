package com.vanik.betroom.modules.room.dao

import androidx.room.*
import com.vanik.betroom.proxy.model.Actor

@Dao
interface ActorDao {
    @Query("SELECT * FROM Actor")
    suspend fun getAllActors(): List<Actor>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actor: Actor)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(actor: Actor)
}