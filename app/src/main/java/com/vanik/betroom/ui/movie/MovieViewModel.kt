package com.vanik.betroom.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.vanik.betroom.db.repository.Repository
import com.vanik.betroom.entity.Actor
import com.vanik.betroom.entity.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    val movies = arrayListOf<Movie>()
    var isRoom = true

    fun insertMovie(actor: Actor, movie: Movie) {
        viewModelScope.launch {
            if (isRoom) {
                Repository.insertMovieInRoomDb(actor, movie)
            } else {
                Repository.insertMovieInSqlLiteDb(actor, movie)
            }
        }
    }


    suspend fun getMovies() = when(isRoom) {
            true->Repository.getMoviesFromRoom()
            else->Repository.getMoviesFromSqlLIte()
        }

}