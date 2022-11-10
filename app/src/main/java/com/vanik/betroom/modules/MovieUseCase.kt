package com.vanik.betroom.modules

import com.vanik.betroom.modules.repository.Repository
import com.vanik.betroom.proxy.model.Actor
import com.vanik.betroom.proxy.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.function.BiPredicate

private val backgroundThread = Dispatchers.IO

class GetMovieUseCase(private val repository: Repository) {
    fun executeInRoom() = repository.getMoviesFromRoom().flowOn(backgroundThread)

    fun executeInSqlLite() = repository.getMoviesFromSqlLIte().flowOn(backgroundThread)
}

class AddMovieUseCase(private val repository: Repository) {
    suspend fun executeInRoom(actor: Actor, movie: Movie) : Boolean {
        var insertIsSuccess= false
        try {
            insertIsSuccess = repository.insertMovieInRoomDb(actor, movie)
        } catch (e: java.lang.Exception) {

        }
            return insertIsSuccess
    }

    suspend fun executeInSqlLite(actor: Actor, movie: Movie) : Boolean {
        var insertIsSuccess= false
        try {
            insertIsSuccess =  repository.insertMovieInSqlLiteDb(actor, movie)
        } catch (e: java.lang.Exception) {

        }
        return insertIsSuccess
    }
}