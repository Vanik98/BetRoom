package com.vanik.betroom.modules.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.vanik.betroom.modules.room.dao.ActorDao
import com.vanik.betroom.modules.room.dao.MovieDao
import com.vanik.betroom.modules.sqllite.DBHelper
import com.vanik.betroom.proxy.model.Actor
import com.vanik.betroom.proxy.model.Movie
import com.vanik.betroom.proxy.model.Pet
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Repository(
    private val actorDao: ActorDao,
    private val movieDao: MovieDao,
    private val dbLite: DBHelper
) {

    suspend fun insertActorInRoomDb(actor: Actor) {
        actorDao.insert(actor)
    }

    suspend fun insertMovieInRoomDb(actor: Actor, movie: Movie) {
        movieDao.insert(movie)
        actorDao.update(actor)
    }

    fun getAllActorsFromRoom() = flow { emit(actorDao.getAllActors()) }
    fun getMoviesFromRoom() = flow { emit(movieDao.getAllMovies()) }

    suspend fun insertActorInSqlLiteDb(actor: Actor) {
        val cv = ContentValues()
        cv.put("name", actor.name)
        cv.put("surname", actor.surname)
        cv.put("age", actor.age)
        val petJson: String = Json.encodeToString(actor.pets)
        cv.put("pets", petJson)
        dbLite.writableDatabase.insert("actor", null, cv)
    }

    suspend fun insertMovieInSqlLiteDb(actor: Actor, movie: Movie) {
        //movie insert
        var cv = ContentValues()
        cv.put("id", movie.id)
        cv.put("name", movie.name)
        cv.put("imdbRate", movie.imdbRate)
        cv.put("actorName", actor.name)
        dbLite.writableDatabase.insert("movie", null, cv)
        //actor update
        cv = ContentValues()
        cv.put("name", actor.name)
        cv.put("surname", actor.surname)
        cv.put("age", actor.age)
        val petJson: String = Json.encodeToString(actor.pets)
        cv.put("pets", petJson)
        val movieIdsJson: String = Json.encodeToString(actor.movieIds)
        cv.put("movieIds", movieIdsJson)
        dbLite.writableDatabase.update("actor", cv, "name= ?", arrayOf(actor.name))
    }

    @SuppressLint("Recycle")
    fun getAllActorsFromSqlLite() = flow {
        val cursorCourses: Cursor = dbLite.writableDatabase.rawQuery("SELECT * FROM Actor", null)
        val actors = arrayListOf<Actor>()
        if (cursorCourses.moveToFirst()) {
            do {
                var pets: List<Pet?>? = arrayListOf()
                if (cursorCourses.getString(3) != null) {
                    val petsJson: List<Pet?>? = Json.decodeFromString(cursorCourses.getString(3))
                    pets = petsJson
                }
                var movieIds: List<Int> = arrayListOf()
                if (cursorCourses.getString(4) != null) {
                    val movieIdsJson: List<Int> = Json.decodeFromString(cursorCourses.getString(4))
                    movieIds = movieIdsJson
                }
                val actor = Actor(
                    name = cursorCourses.getString(0),
                    surname = cursorCourses.getString(1),
                    age = cursorCourses.getInt(2),
                    pets = pets,
                    movieIds = movieIds
                )
                actors.add(actor)
            } while (cursorCourses.moveToNext())
        }
        emit(actors)
    }

    fun getMoviesFromSqlLIte() = flow {
        val cursorCourses: Cursor = dbLite.writableDatabase.rawQuery("SELECT * FROM Movie", null)
        val movies = arrayListOf<Movie>()
        if (cursorCourses.moveToFirst()) {
            do {
                val movie = Movie(
                    id = cursorCourses.getInt(0),
                    name = cursorCourses.getString(1),
                    imdbRate = cursorCourses.getDouble(2),
                    actorName = cursorCourses.getString(3)
                )
                movies.add(movie)
            } while (cursorCourses.moveToNext())
        }
        emit(movies)
    }
}