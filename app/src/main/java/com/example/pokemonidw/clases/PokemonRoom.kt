package com.example.pokemonidw.clases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonRoom(
    @PrimaryKey val numero: Int,
    @ColumnInfo(name = "nombre") val nombre: String?,
    @ColumnInfo(name="especie1") val especie1:String?,
    @ColumnInfo(name="especie2") val especie2:String?,
    @ColumnInfo(name="weight") val weight:Int?,
    @ColumnInfo(name="habilidades") val habilidades:String?,
    @ColumnInfo(name="habilidades2") val habilidades2:String?



    )