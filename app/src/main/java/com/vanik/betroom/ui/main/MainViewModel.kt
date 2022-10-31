package com.vanik.betroom.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.vanik.betroom.entity.Actor
import com.vanik.betroom.db.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var actors = arrayListOf<Actor>()
    var actorsRoom = arrayListOf<Actor>()
    var actorsLite = arrayListOf<Actor>()
    var isRoom = true

    fun connectRepository(context: Context) {
        Repository.buildRepo(context)
    }

    fun insertActor(actor: Actor) = viewModelScope.launch {
        if (isRoom) {
            actorsRoom.add(0, actor)
            Repository.insertActorWithRoom(actor)
        } else {
            actorsLite.add(0, actor)
            Repository.insertActorWithSqlLite(actor)
        }
    }

    fun getAllRoomActors() = liveData(Dispatchers.IO) { emit(Repository.getAllActorsWithRoom()) }

    fun getAllLiteActors() = liveData(Dispatchers.IO) { emit(Repository.getAllActorsSqlLite()) }
}