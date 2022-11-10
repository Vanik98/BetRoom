package com.vanik.betroom.proxy.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
@Entity(
    foreignKeys = [ForeignKey(
        entity = Actor::class,
        parentColumns = ["name"],
        childColumns = ["actorName"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Movie(
    @PrimaryKey
    val id: Int,
    val name: String,
    val imdbRate: Double,
    val actorName: String
)