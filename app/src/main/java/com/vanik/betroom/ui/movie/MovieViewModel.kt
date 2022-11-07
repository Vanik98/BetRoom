package com.vanik.betroom.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanik.betroom.proxy.model.Actor
import com.vanik.betroom.proxy.model.Movie
import com.vanik.betroom.ui.AddMovieUseCase
import com.vanik.betroom.ui.GetMovieUseCase
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