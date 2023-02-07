package com.pokemon.data.repository

import androidx.annotation.WorkerThread
import com.pokemon.data.DataState
import com.pokemon.data.remote.*
import com.pokemon.data.remote.responses.PokemonModel
import com.pokemon.model.Pokemon
import com.pokemon.utils.StringUtils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import java.io.IOException
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val stringUtils: StringUtils,
    private val apiService: PokemonApiService
) : PokemonRepository {
    @WorkerThread
    override suspend fun loadPokemon(
        offset: Int,
        limit: Int
    ): Flow<DataState<PokemonModel>> {
        return flow {
            apiService.loadPokemon(offset, limit).apply {
                this.onSuccessSuspend {
                    data?.let { emit(DataState.success(it)) }

                }
            }.onErrorSuspend {
                emit(DataState.error(message()))

                // handle the case when the API request gets an exception response.
                // e.g. network connection error.
            }.onExceptionSuspend {
                if (this.exception is IOException) {
                    emit(DataState.error(stringUtils.noNetworkErrorMessage()))
                } else {
                    emit(DataState.error(stringUtils.somethingWentWrong()))
                }
            }
        }
    }


    override suspend fun changePokemon(id: Int): Flow<DataState<Pokemon>> {
        return flow {
            apiService.getPokemon(id).apply {
                this.onSuccessSuspend {
                    data?.let {
                        emit(DataState.success(it))
                    }
                }
            }.onErrorSuspend {
                emit(DataState.error(message()))

                // handle the case when the API request gets an exception response.
                // e.g. network connection error.
            }.onExceptionSuspend {
                if (this.exception is IOException) {
                    emit(DataState.error(stringUtils.noNetworkErrorMessage()))
                } else {
                    emit(DataState.error(stringUtils.somethingWentWrong()))
                }
            }
        }
    }
}