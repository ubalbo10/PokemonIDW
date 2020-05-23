package com.example.pokemonidw.clases

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class DetallePokemon(
    @SerializedName("abilities")val abilities : List<Abilities>,
    @SerializedName("base_experience") val base_experience : Int,

    @SerializedName( "height") val height : Int,
    @SerializedName("held_items") val held_items : List<String>,
    @SerializedName("id") val id : Int,
    @SerializedName("is_default")val is_default : Boolean,
    @SerializedName("location_area_encounters")val location_area_encounters : String,
    @SerializedName("name")val name : String,
    @SerializedName("order")val order : Int,
    @SerializedName("species")val species : Species,
    @SerializedName( "weight")val weight : Int


)