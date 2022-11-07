package com.vanik.betroom.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanik.betroom.modules.AddMovieUseCase
import com.vanik.betroom.modules.GetMovieUseCase
import com.vanik.betroom.proxy.model.Actor
import com.vanik.betroom.proxy.model.Movie
import kotlinx.coroutines.launch

class MovieViewModel(val addMovieUseCase: AddMovieUseCase,val getMovieUseCase: GetMovieUseCase) : ViewModel() {
    val movies = arrayListOf<Movie>()
    var isRoom = true

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