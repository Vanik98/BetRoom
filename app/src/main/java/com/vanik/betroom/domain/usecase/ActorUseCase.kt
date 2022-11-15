package com.vanik.betroom.domain.usecase

import com.vanik.betroom.data.repository.Repository
import com.vanik.growdb.model.Actor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

private val backgroundThread = Dispatchers.IO

class GetActorUseCase(private val repository: Repository) {

    fun executeInRoom() = repository.getAllActorsFromRoom().flowOn(backgroundThread)

    fun executeInSqlLite() = repository.getAllActorsFromSqlLite().flowOn(backgroundThread)
}

class AddActorUseCase(private val repository: Repository) {
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