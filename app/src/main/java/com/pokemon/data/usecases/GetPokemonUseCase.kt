package com.pokemon.data.usecases

import com.pokemon.data.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonUseCase @Inject constructor(private val repository: PokemonRepository) {
    suspend operator fun invoke(
        offset: Int = 0,
        limit: Int,
    ) = repository.loadPokemon(
        offset = offset,
        limit = limit
    )
}