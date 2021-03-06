package com.example.pokemonidw.Interfaces

import androidx.room.*
import com.example.pokemonidw.clases.Favoritos
import com.example.pokemonidw.clases.PokemonRoom


@Dao
interface PokeDao {
    @Query("SELECT * FROM PokemonRoom")
    fun getAll(): List<PokemonRoom>


    @Query("SELECT * FROM PokemonRoom WHERE numero = (:id)")
    fun loadAllByIds(id: Int): PokemonRoom

    @Insert
    fun insertAll(vararg pokemon: PokemonRoom)

    @Delete
    fun delete(pokemon: PokemonRoom)
    @Update
    fun updatePok(pokemon: PokemonRoom):Int

}