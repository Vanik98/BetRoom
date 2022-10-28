package com.vanik.betroom.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@kotlinx.serialization.Serializable
@Entity
data class Movie(
    @PrimaryKey
    val id : Int,
    val name : String,
    val imdbRate: Double
)