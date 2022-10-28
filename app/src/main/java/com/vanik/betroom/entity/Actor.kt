package com.vanik.betroom.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@kotlinx.serialization.Serializable
@Entity
data class Actor(
    @PrimaryKey
    val name :String,
    val surname : String,
    var age :Int,
    val pets : List<Pet?>?,
    var movieIds : List<Int>?,
)
