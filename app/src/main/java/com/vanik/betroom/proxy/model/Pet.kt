package com.vanik.betroom.proxy.model

import androidx.room.PrimaryKey

@kotlinx.serialization.Serializable
data class Pet(
    @PrimaryKey
    val id: Int,
    val name: String,
    val age: Int ,
    val isSmart: Boolean
)
