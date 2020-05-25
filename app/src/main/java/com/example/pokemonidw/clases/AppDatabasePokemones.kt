package com.example.pokemonidw.clases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokemonidw.Interfaces.PokeDao
import com.example.pokemonidw.Interfaces.favDao

@Database(entities = arrayOf(PokemonRoom::class), version = 1)
abstract class AppDatabasePokemones : RoomDatabase() {
    abstract fun PokeDao(): PokeDao
}