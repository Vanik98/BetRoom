package com.vanik.betroom.domain.usecase

import com.vanik.betroom.data.repository.Repository
import com.vanik.growdb.model.Actor
import com.vanik.growdb.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

private val backgroundThread = Dispatchers.IO

class GetMovieUseCase(private val repository: Repository) {
    fun executeInRoom() = repository.getMoviesFromRoom().flowOn(backgroundThread)

    fun executeInSqlLite() = repository.getMoviesFromSqlLIte().flowOn(backgroundThread)
}

class AddMovieUseCase(private val repository: Repository) {
    suspend fun executeInRoom(actor: Actor, movie: Movie) = withContext(backgroundThread) {
        try {
            repository.insertMovieInRoomDb(actor, movie)
        } catch (e: java.lang.Exception) {

        }

    }

    suspend fun executeInSqlLite(actor: Actor, movie: Movie) = withContext(backgroundThread) {
        try {
            repository.insertMovieInSqlLiteDb(actor, movie)
        } catch (e: java.lang.Exception) {

        }
    }
}