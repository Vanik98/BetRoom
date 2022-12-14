package com.vanik.betroom.data.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.vanik.growdb.model.Actor
import com.vanik.growdb.model.Movie
import com.vanik.growdb.model.Pet
import com.vanik.growdb.room.dao.ActorDao
import com.vanik.growdb.room.dao.MovieDao
import com.vanik.growdb.sqllite.DBHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Repository(
    private val ioDispatcher: CoroutineDispatcher,
    private val actorDao: ActorDao,
    private val movieDao: MovieDao,
    private val dbLite: DBHelper
) {
    suspend fun insertActorInRoomDb(actor: Actor) = withContext(ioDispatcher) {
        actorDao.insert(actor)
    }

    suspend fun insertMovieInRoomDb(actor: Actor, movie: Movie) = withContext(ioDispatcher) {
        movieDao.insert(movie)
        actorDao.update(actor)
    }

    fun getAllActorsFromRoom() = flow { emit(actorDao.getAllActors()) }.flowOn(ioDispatcher)
    fun getMoviesFromRoom() = flow { emit(movieDao.getAllMovies()) }.flowOn(ioDispatcher)

    suspend fun insertActorInSqlLiteDb(actor: Actor) = withContext(ioDispatcher) {
        val cv = ContentValues()
        cv.put("name", actor.name)
        cv.put("surname", actor.surname)
        cv.put("age", actor.age)
        val petJson: String = Json.encodeToString(actor.pets)
        cv.put("pets", petJson)
        dbLite.writableDatabase.insert("actor", null, cv)
    }

    suspend fun insertMovieInSqlLiteDb(actor: Actor, movie: Movie) = withContext(ioDispatcher) {
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
    }.flowOn(ioDispatcher)

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
    }.flowOn(ioDispatcher)
}