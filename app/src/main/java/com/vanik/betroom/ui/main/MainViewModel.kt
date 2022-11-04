package com.vanik.betroom.ui.main

import android.content.Context
import androidx.core.os.trace
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.vanik.betroom.entity.Actor
import com.vanik.betroom.db.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val actorsLiveData: MutableLiveData<List<Actor>> = MutableLiveData()
    var actors = arrayListOf<Actor>()
    private val actorsRoom = arrayListOf<Actor>()
    private val actorsSqlLite = arrayListOf<Actor>()

    fun connectRepository(context: Context) {
        Repository.buildRepo(context)
        fetchData()
    }

    private fun fetchData() {
        fetchAllRoomActors()
        fetchAllLiteActors()
    }

    private fun fetchAllRoomActors() = viewModelScope.launch {
        Repository.getAllActorsWithRoom().collect {
            for (i in it.indices) {
                actorsRoom.add(it[it.size - i - 1])
            }
            actors.addAll(actorsRoom)
            actorsLiveData.value = actors
        }
    }

    private fun fetchAllLiteActors() = viewModelScope.launch {
        Repository.getAllActorsSqlLite().collect {
            for (i in it.indices) {
                actorsSqlLite.add(it[it.size - i - 1])
            }
        }
    }

    fun insertActor(isRoom : Boolean,actor: Actor) = viewModelScope.launch {
        when (isRoom) {
            true -> {
                Repository.insertActorWithRoom(actor)
                actorsRoom.add(0, actor)
            }
            false -> {
                Repository.insertActorWithSqlLite(actor)
                actorsSqlLite.add(0, actor)
            }
        }
        actors.add(0, actor)
        actorsLiveData.value = actors
    }

    fun chooseDb(isRoom : Boolean) {
        actors.clear()
        when (isRoom) {
            true -> { actors.addAll(actorsRoom) }
            false -> { actors.addAll(actorsSqlLite) }
        }
    }

}