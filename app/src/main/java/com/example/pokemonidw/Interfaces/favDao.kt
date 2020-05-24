package com.example.pokemonidw.Interfaces

import androidx.room.*
import com.example.pokemonidw.clases.Favoritos

@Dao
interface favDao {
    @Query("SELECT * FROM Favoritos")
    fun getAll(): List<Favoritos>

    @Query("SELECT * FROM Favoritos WHERE esFavorito IN (:valor)")
    fun loadAllByIds(valor: Boolean): List<Favoritos>



    @Insert
    fun insertAll(vararg favorito: Favoritos)

    @Delete
    fun delete(favorito: Favoritos)
    @Update
    fun updateFav(favorito:Favoritos):Int

}