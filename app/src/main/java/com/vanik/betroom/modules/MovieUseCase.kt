package com.vanik.betroom.ui


import com.vanik.betroom.modules.repository.Repository
import com.vanik.betroom.proxy.model.Actor
import com.vanik.betroom.proxy.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

private val backgroundThread = Dispatchers.IO

class GetMovieUseCase() {
    fun executeInRoom() = Repository.getMoviesFromRoom().flowOn(backgroundThread)

    fun executeInSqlLite() = Repository.getMoviesFromSqlLIte().flowOn(backgroundThread)
}

class AddMovieUseCase {
    suspend fun executeInRoom(actor: Actor, movie: Movie) = withContext(backgroundThread) {
        try {
            Repository.insertMovieInRoomDb(actor, movie)
        } catch (e: java.lang.Exception) {

        }

    }

    suspend fun executeInSqlLite(actor: Actor, movie: Movie) = withContext(backgroundThread) {
        try {
            Repository.insertMovieInSqlLiteDb(actor, movie)
        } catch (e: java.lang.Exception) {

        }
    }
}