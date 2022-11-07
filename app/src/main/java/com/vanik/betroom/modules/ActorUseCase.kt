package com.vanik.betroom.modules

import com.vanik.betroom.modules.repository.Repository
import com.vanik.betroom.proxy.model.Actor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

private val backgroundThread = Dispatchers.IO

class GetActorUseCase(val repository: Repository) {

    fun executeInRoom() = repository.getAllActorsFromRoom().flowOn(backgroundThread)

    fun executeInSqlLite() = repository.getAllActorsFromSqlLite().flowOn(backgroundThread)
}

class AddActorUseCase(val repository: Repository) {
    suspend fun executeInRoom(actor: Actor) = withContext(backgroundThread) {
        try {
            repository.insertActorInRoomDb(actor)
        } catch (e: java.lang.Exception) {

        }
    }

    suspend fun executeInSqlLite(actor: Actor) = withContext(backgroundThread) {
        try {
            repository.insertActorInSqlLiteDb(actor)
        } catch (e: java.lang.Exception) {

        }
    }
}