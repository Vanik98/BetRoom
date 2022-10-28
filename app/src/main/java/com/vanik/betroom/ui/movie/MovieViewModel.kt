package com.vanik.betroom.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanik.betroom.db.repository.Repository
import com.vanik.betroom.entity.Actor
import com.vanik.betroom.entity.Movie
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    val movies = arrayListOf<Movie>()
    var isRoom = true

    fun insertMovie(actor: Actor, movie: Movie) {
        viewModelScope.launch {
            if (isRoom) {
                Repository.insertMovieWithRoom(actor, movie)
            } else {
                Repository.insertMovieWithSqlLite(actor, movie)
            }
        }
    }

    fun getMovies(): LiveData<List<Movie>> {
        return if (isRoom) {
            Repository.getMoviesWithRoom()
        } else {
            Repository.getMoviesSqlLIte()
        }
    }
}