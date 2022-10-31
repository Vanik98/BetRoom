package com.vanik.betroom.db.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vanik.betroom.db.room.AppDatabase
import com.vanik.betroom.db.sqllite.DBHelper
import com.vanik.betroom.entity.Actor
import com.vanik.betroom.entity.Movie
import com.vanik.betroom.entity.Pet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


object Repository {
    private lateinit var dbRoom: AppDatabase
    private lateinit var dbLite: SQLiteDatabase

    fun buildRepo(context: Context) {
        if (!this::dbRoom.isInitialized) {
            dbRoom = AppDatabase.getInstance(context)
        }
        if (!this::dbLite.isInitialized) {
            dbLite = DBHelper.getInstance(context).writableDatabase
        }
    }

    suspend fun insertActorWithRoom(actor: Actor) {
        dbRoom.ActorDao().insert(actor)
    }

    suspend fun insertActorWithSqlLite(actor: Actor) = withContext(Dispatchers.IO) {
        val cv = ContentValues()
        cv.put("name", actor.name)
        cv.put("surname", actor.surname)
        cv.put("age", actor.age)
        val petJson: String = Json.encodeToString(actor.pets)
        cv.put("pets", petJson)
        dbLite.insert("actor", null, cv)
    }

    suspend fun insertMovieWithRoom(actor: Actor, movie: Movie) = withContext(Dispatchers.IO) {
        dbRoom.MovieDao().insert(movie)
        dbRoom.ActorDao().update(actor)
    }

    suspend fun insertMovieWithSqlLite(actor: Actor, movie: Movie) = withContext(Dispatchers.IO) {
        //movie insert
        var cv = ContentValues()
        cv.put("id", movie.id)
        cv.put("name", movie.name)
        cv.put("imdbRate", movie.imdbRate)
        dbLite.insert("movie", null, cv)
        //actor update
        cv = ContentValues()
        cv.put("name", actor.name)
        cv.put("surname", actor.surname)
        cv.put("age", actor.age)
        val petJson: String = Json.encodeToString(actor.pets)
        cv.put("pets", petJson)
        val movieIdsJson: String = Json.encodeToString(actor.movieIds)
        cv.put("movieIds", movieIdsJson)
        dbLite.update("actor", cv, "name= ?", arrayOf(actor.name))
    }

    suspend fun getAllActorsWithRoom() = dbRoom.ActorDao().getAllActors()

    @SuppressLint("Recycle")
   suspend fun getAllActorsSqlLite() : List<Actor> {
        val cursorCourses: Cursor = dbLite.rawQuery("SELECT * FROM Actor", null)
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
        return actors
    }

    suspend fun getMoviesWithRoom() = dbRoom.MovieDao().getAllMovies()

   suspend fun getMoviesSqlLIte(): List<Movie> {
        val cursorCourses: Cursor = dbLite.rawQuery("SELECT * FROM Movie", null)
        val movies = arrayListOf<Movie>()
        if (cursorCourses.moveToFirst()) {
            do {
                val movie = Movie(
                    id = cursorCourses.getInt(0),
                    name = cursorCourses.getString(1),
                    imdbRate = cursorCourses.getDouble(2)
                )
                movies.add(movie)
            } while (cursorCourses.moveToNext())
        }
        return movies
    }
}