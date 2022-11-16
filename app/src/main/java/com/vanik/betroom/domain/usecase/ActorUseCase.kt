package com.vanik.betroom.domain.usecase

import com.vanik.betroom.data.repository.Repository
import com.vanik.growdb.model.Actor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


class GetActorUseCase(private val repository: Repository) {

    fun executeInRoom() = repository.getAllActorsFromRoom()

    fun executeInSqlLite() = repository.getAllActorsFromSqlLite()
}

class AddActorUseCase(private val repository: Repository) {
    suspend fun executeInRoom(actor: Actor)  {
        try {
            repository.insertActorInRoomDb(actor)
        } catch (e: java.lang.Exception) {

        }
    }

    suspend fun executeInSqlLite(actor: Actor)  {
        try {
            repository.insertActorInSqlLiteDb(actor)
        } catch (e: java.lang.Exception) {

        }
    }
}