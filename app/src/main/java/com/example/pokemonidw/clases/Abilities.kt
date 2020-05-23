package com.example.pokemonidw.clases

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class Abilities(

    @SerializedName("is_hidden")val is_hidden : Boolean,
    @SerializedName("slot")val slot : Int,
    @SerializedName("ability")val ability : Ability
)