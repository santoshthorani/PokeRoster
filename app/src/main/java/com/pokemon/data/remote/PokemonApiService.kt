package com.pokemon.data.remote

import com.pokemon.data.remote.responses.PokemonModel
import com.pokemon.model.Pokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {
    companion object {
        const val BASE_API_URL = "https://pokeapi.co/api/v2/";
    }
    @GET("pokemon")
   suspend  fun loadPokemon(
        @Query("offset") offset :Int =0,
        @Query("limit")limit :Int =151,

    ): ApiResponse<PokemonModel>

   @GET("pokemon/{id}")
   suspend fun  getPokemon(@Path("id") id:Int):ApiResponse<Pokemon>
}