package com.example.pokemonidw.clases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokemonidw.Interfaces.favDao

@Database(entities = arrayOf(Favoritos::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favDao(): favDao
}