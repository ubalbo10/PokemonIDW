package com.example.pokemonidw.Interfaces


import com.example.pokemonidw.clases.DetallePokemon
import com.example.pokemonidw.clases.ListaPokemon
import com.example.pokemonidw.clases.PokemonEspecie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    //lista de pokemones
    @GET("pokemon/")
    fun ObtenerListadoDePokemones():retrofit2.Call<ListaPokemon>?
    @GET
    fun DetallePokemon(@Url url:String):Call<DetallePokemon>?
    @GET
    fun EspeciesPokemon(@Url url:String):Call<PokemonEspecie>?

    companion object {
        const val URL_BASE = "https://pokeapi.co/api/v2/"


    }
}