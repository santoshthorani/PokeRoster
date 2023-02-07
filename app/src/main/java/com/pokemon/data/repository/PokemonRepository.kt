package com.pokemon.data.repository

import com.pokemon.data.DataState
import com.pokemon.data.remote.responses.PokemonModel
import com.pokemon.model.Pokemon
import kotlinx.coroutines.flow.Flow


interface PokemonRepository {
    suspend fun loadPokemon(offset: Int, limit:Int)
    : Flow<DataState<PokemonModel>>
    suspend fun changePokemon(id:Int)
            : Flow<DataState<Pokemon>>
}
