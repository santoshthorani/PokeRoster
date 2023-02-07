package com.pokemon.data.usecases

import com.pokemon.data.repository.PokemonRepository
import javax.inject.Inject

class ChangePokemonUseCase @Inject constructor(private val repository: PokemonRepository) {
    suspend operator fun invoke(
        id: Int
    ) = repository.changePokemon(
        id = id
    )
}