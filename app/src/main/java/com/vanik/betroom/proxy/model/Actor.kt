package com.vanik.betroom.proxy.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@kotlinx.serialization.Serializable
@Entity
data class Actor(
    @PrimaryKey
    var name :String,
    val surname : String,
    var age :Int,
    val pets : List<Pet?>?,
    var movieIds : List<Int>?,
)
