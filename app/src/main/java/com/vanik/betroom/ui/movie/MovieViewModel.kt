package com.vanik.betroom.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.vanik.betroom.db.repository.*
import com.vanik.betroom.entity.Actor
import com.vanik.betroom.entity.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    val movies = arrayListOf<Movie>()
    var isRoom = true
    private  val addMovieUseCase: AddMovieUseCase = AddMovieUseCase()
    private  val getMovieUseCase: GetMovieUseCase = GetMovieUseCase()

    fun insertMovie(actor: Actor, movie: Movie) {
        viewModelScope.launch {
            if (isRoom) {
                addMovieUseCase.executeInRoom(actor, movie)
            } else {
                addMovieUseCase.executeInSqlLite(actor, movie)
            }
        }
    }


    fun getMovies() = when (isRoom) {
        true -> getMovieUseCase.executeInRoom()
        else -> getMovieUseCase.executeInSqlLite()
    }

}