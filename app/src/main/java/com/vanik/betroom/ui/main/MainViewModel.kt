package com.vanik.betroom.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanik.betroom.modules.AddActorUseCase
import com.vanik.betroom.modules.GetActorUseCase
import com.vanik.betroom.proxy.model.Actor
import kotlinx.coroutines.launch

class MainViewModel(
    private val addActorUseCase: AddActorUseCase,
    private val getAddActorUseCase: GetActorUseCase
) : ViewModel() {
    val actorsLiveData: MutableLiveData<List<Actor>> = MutableLiveData()
    var actors = arrayListOf<Actor>()
    private val actorsRoom = arrayListOf<Actor>()
    private val actorsSqlLite = arrayListOf<Actor>()
    var insertOk = true

    fun fetchData() {
        fetchAllRoomActors()
        fetchAllLiteActors()
    }

    private fun fetchAllRoomActors() = viewModelScope.launch {
        getAddActorUseCase.executeInRoom().collect {
            for (i in it.indices) {
                actorsRoom.add(it[it.size - i - 1])
            }
            actors.addAll(actorsRoom)
            actorsLiveData.value = actors
        }
    }

    private fun fetchAllLiteActors() = viewModelScope.launch {
        getAddActorUseCase.executeInSqlLite().collect {
            for (i in it.indices) {
                actorsSqlLite.add(it[it.size - i - 1])
            }
        }
    }

    fun insertActor(isRoom: Boolean, actor: Actor) = viewModelScope.launch {
        for (i in actors){
            if(i.name == actor.name){
                 insertOk = false
            }
        }
        if(insertOk) {
            when (isRoom) {
                true -> {
                    addActorUseCase.executeInRoom(actor)
                    actorsRoom.add(0, actor)
                }
                false -> {
                    addActorUseCase.executeInSqlLite(actor)
                    actorsSqlLite.add(0, actor)
                }
            }
            actors.add(0, actor)
            actorsLiveData.value = actors
        }
    }

    fun chooseDb(isRoom: Boolean) {
        actors.clear()
        when (isRoom) {
            true -> {
                actors.addAll(actorsRoom)
            }
            false -> {
                actors.addAll(actorsSqlLite)
            }
        }
    }

}