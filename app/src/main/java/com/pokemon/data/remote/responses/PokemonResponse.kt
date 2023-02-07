package com.pokemon.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.pokemon.model.Pokemon

data class PokemonModel(

    @SerializedName("count") var count: Int? = null,
    @SerializedName("next") var next: String? = null,
    @SerializedName("previous") var previous: String? = null,
    @SerializedName("results") var results: ArrayList<Pokemon> = arrayListOf()

)