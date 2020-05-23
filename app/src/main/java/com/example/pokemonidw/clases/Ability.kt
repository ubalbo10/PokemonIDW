package com.example.pokemonidw.clases

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class Ability (

    @SerializedName("name")val name : String,
    @SerializedName("url")val url:String

)
