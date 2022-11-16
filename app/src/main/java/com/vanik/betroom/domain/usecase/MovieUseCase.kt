package com.vanik.betroom.domain.usecase

import com.vanik.betroom.data.repository.Repository
import com.vanik.growdb.model.Actor
import com.vanik.growdb.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class GetMovieUseCase(private val repository: Repository) {
    fun executeInRoom() = repository.getMoviesFromRoom()

    fun executeInSqlLite() = repository.getMoviesFromSqlLIte()
}

class AddMovieUseCase(private val repository: Repository) {
    suspend fun executeInRoom(actor: Actor, movie: Movie) {
        try {
            repository.insertMovieInRoomDb(actor, movie)
        } catch (e: java.lang.Exception) {

        }
    }

    suspend fun executeInSqlLite(actor: Actor, movie: Movie)  {
        try {
            repository.insertMovieInSqlLiteDb(actor, movie)
        } catch (e: java.lang.Exception) {

        }
    }
}