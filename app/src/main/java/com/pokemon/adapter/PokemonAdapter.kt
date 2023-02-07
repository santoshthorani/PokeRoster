package com.pokemon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.pokemon.databinding.ItemPokemonBinding
import com.pokemon.model.Pokemon
import com.pokemon.utils.Constants
import com.pokemon.utils.getIdFromUrl

class PokemonAdapter(val onPokemonChange: (photo: Pokemon, position: Int) -> Unit) :
    RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {
    private val pokemonItems: ArrayList<Pokemon> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {

        val binding = ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonViewHolder(binding)
    }

    fun updateItems(pokemonList: List<Pokemon>) {
        pokemonItems.clear()
        pokemonItems.addAll(pokemonList)
        notifyDataSetChanged()
    }

    fun updateItem(pokemon: Pokemon, position: Int) {
        pokemonItems[position] = pokemon
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(pokemonItems[holder.adapterPosition], holder.adapterPosition)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount() = pokemonItems.size
    inner class PokemonViewHolder(val itemPokemonBinding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(itemPokemonBinding.root) {
        fun bind(pokemon: Pokemon, position: Int) {
            itemPokemonBinding.apply {
                pokemon.url?.let { url ->
                    imgPokemon.load(
                        Constants.getImgUrl(
                            getIdFromUrl(url)
                        )
                    )

                }
                tvName.text = pokemon.name
                ivChange.setOnClickListener {
                    onPokemonChange(pokemon, position)
                }
            }
        }
    }
}

