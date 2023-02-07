package com.pokemon.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokemon.data.DataState
import com.pokemon.data.remote.responses.PokemonModel
import com.pokemon.data.usecases.ChangePokemonUseCase
import com.pokemon.data.usecases.GetPokemonUseCase
import com.pokemon.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPokemonUseCase: GetPokemonUseCase,
    private val changePokemonUseCase: ChangePokemonUseCase
) : ViewModel() {
    private var _uiState = MutableLiveData<MainUiState>()
    var uiStateLiveData: LiveData<MainUiState> = _uiState

    private var _pokemon = MutableLiveData<PokemonModel>()
    var pokemonListLiveData: LiveData<PokemonModel> = _pokemon

    private var _pokemonChange = MutableLiveData<Pokemon>()
    var pokemonLiveData: LiveData<Pokemon> = _pokemonChange

    private var offset = 0
    private var limit = 30


    init {
        fetchPokemon()
    }

    private fun fetchPokemon() {
        getPokemon()
    }

    fun loadMorePokemon() {
        if (offset > 150)
            _uiState.postValue(ErrorSecondaryState("More than 151 Pokemon have already been loaded "))
        else {
            offset += limit
            getPokemon()
        }

    }

    fun changePokemon(id: Int) {
        _uiState.postValue(SecondaryLoadingState)
        viewModelScope.launch {
            changePokemonUseCase(id).collect { dataState ->
                when (dataState) {
                    is DataState.Error -> {
                        _uiState.postValue(ErrorSecondaryState(dataState.message))
                    }
                    is DataState.Success -> {
                        _uiState.postValue(ContentState)
                        _pokemonChange.postValue(dataState.data)
                    }
                }
            }
        }
    }

    private fun getPokemon() {
        _uiState.postValue(if (offset == 0) LoadingState else LoadingNextPageState)
        viewModelScope.launch {
            getPokemonUseCase(offset, limit).collect { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        _uiState.postValue(ContentState)
                        _pokemon.postValue(dataState.data)
                        if (offset == 0) {
                            // First page
                            _uiState.postValue(ContentState)
                            _pokemon.postValue(dataState.data)
                        } else {
                            // Any other page
                            _uiState.postValue(ContentNextPageState)
                            val currentList = arrayListOf<Pokemon>()
                            val model = _pokemon.value
                            model?.let {
                                currentList.addAll(it.results)
                            }

                            currentList.addAll(dataState.data.results)
                            model?.results = currentList
                            _pokemon.postValue(model)
                        }

                    }

                    is DataState.Error -> {
                        if (offset == 0) {
                            _uiState.postValue(ErrorState(dataState.message))
                        } else {
                            _uiState.postValue(ErrorNextPageState(dataState.message))
                        }
                    }

                }
            }
        }
    }
}