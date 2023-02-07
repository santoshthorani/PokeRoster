package com.pokemon.utils

class Constants {
    companion object {
        fun getImgUrl(id: Int): String {
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/${id}.png"
        }
    }

}