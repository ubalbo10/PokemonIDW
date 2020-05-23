package com.example.pokemonidw.clases

data class PokemonEspecie (

    val color : Color,
    val egg_groups : List<NameEspecie>,
    val id : Int,
    val generation : Generation,
    val name : String //generacion

)