package com.vanik.betroom.db.room

import androidx.room.TypeConverter
import com.vanik.betroom.entity.Pet
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataConverter {

    @TypeConverter
    fun fromPets(str: String): List<Pet?>? {
        return Json.decodeFromString(str)
    }

    @TypeConverter
    fun stringPets(pets: List<Pet?>?): String {
        return Json.encodeToString(pets)
    }

    @TypeConverter
    fun fromMoviesList(str: String): List<Int> {
        return Json.decodeFromString(str)
    }

    @TypeConverter
    fun stringMoviesList(movieId: List<Int>): String {
        return Json.encodeToString(movieId)
    }

}