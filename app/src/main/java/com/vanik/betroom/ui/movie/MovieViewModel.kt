package com.vanik.betroom.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanik.growdb.model.Actor
import com.vanik.growdb.model.Movie
import com.vanik.betroom.domain.usecase.AddMovieUseCase
import com.vanik.betroom.domain.usecase.GetMovieUseCase
import kotlinx.coroutines.launch

class MovieViewModel(
    private val addMovieUseCase: AddMovieUseCase,
    private val getMovieUseCase: GetMovieUseCase
) : ViewModel() {
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