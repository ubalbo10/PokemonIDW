package com.example.pokemonidw.clases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favoritos(
    @PrimaryKey val numero: Int,
    @ColumnInfo(name = "esFavorito") val esFavorito: Boolean?

)