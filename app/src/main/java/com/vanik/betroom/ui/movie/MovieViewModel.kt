package com.vanik.betroom.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanik.betroom.modules.AddMovieUseCase
import com.vanik.betroom.modules.GetMovieUseCase
import com.vanik.betroom.proxy.model.Actor
import com.vanik.betroom.proxy.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel(
    private val addMovieUseCase: AddMovieUseCase,
    private val getMovieUseCase: GetMovieUseCase
) : ViewModel() {
    val movies = arrayListOf<Movie>()
    lateinit var actor: Actor
    var isRoom = true

    fun insertMovie(actor: Actor, movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            var insertIsSuccess = false
            insertIsSuccess = if (isRoom) {
                addMovieUseCase.executeInRoom(actor, movie)
            } else {
                addMovieUseCase.executeInSqlLite(actor, movie)
            }
            if(insertIsSuccess) {
                if (actor.movieIds == null) {
                    actor.movieIds = arrayListOf()
                }
                (actor.movieIds as ArrayList<Int>).add(movie.id)
                movies.add(0, movie)
            }
        }
    }

    fun getMovies() = when (isRoom) {
        true -> getMovieUseCase.executeInRoom()
        else -> getMovieUseCase.executeInSqlLite()
    }

}