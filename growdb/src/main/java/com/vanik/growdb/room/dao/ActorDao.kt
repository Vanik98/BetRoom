package com.vanik.growdb.room.dao

import androidx.room.*
import com.vanik.growdb.model.Actor

@Dao
interface ActorDao {
    @Query("SELECT * FROM Actor")
    suspend fun getAllActors(): List<Actor>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(actor: Actor)

    @Update
    suspend fun update(actor: Actor)
}