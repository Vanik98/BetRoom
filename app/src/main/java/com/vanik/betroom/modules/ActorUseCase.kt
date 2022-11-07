package com.vanik.betroom.ui

import com.vanik.betroom.modules.repository.Repository
import com.vanik.betroom.proxy.model.Actor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

private val backgroundThread = Dispatchers.IO

class GetActorUseCase() {

    fun executeInRoom() = Repository.getAllActorsFromRoom().flowOn(backgroundThread)

    fun executeInSqlLite() = Repository.getAllActorsFromSqlLite().flowOn(backgroundThread)
}

class AddActorUseCase {
    suspend fun executeInRoom(actor: Actor) = withContext(backgroundThread) {
        try {
            Repository.insertActorInRoomDb(actor)
        } catch (e: java.lang.Exception) {

        }
    }

    suspend fun executeInSqlLite(actor: Actor) = withContext(backgroundThread) {
        try {
            Repository.insertActorInSqlLiteDb(actor)
        } catch (e: java.lang.Exception) {

        }
    }
}