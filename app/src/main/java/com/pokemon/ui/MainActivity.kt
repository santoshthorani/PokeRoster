package com.pokemon.ui

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.pokemon.R
import com.pokemon.adapter.PokemonAdapter
import com.pokemon.databinding.ActivityMainBinding
import com.pokemon.utils.getIdFromUrl
import com.pokemon.utils.gone
import com.pokemon.utils.showToast
import com.pokemon.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var pokemonAdapter: PokemonAdapter
    var position: Int = 0
    var id: Int = 0
    lateinit var bi: ActivityMainBinding

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bi = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bi.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setUpViews()
        initObservations()
    }

    private fun initObservations() {
        viewModel.uiStateLiveData.observe(this) { state ->
            when (state) {

                ContentState -> {
                    bi.rvPokemon.visible()
                    bi.loader.gone()
                    bi.tvError.gone()
                }
                is ErrorState -> {
                    bi.loader.gone()

                    bi.rvPokemon.gone()

                    bi.tvError.visible()
                    bi.tvError.text = state.message

                }
                is LoadingState -> {
                    bi.loader.visible()
                    bi.rvPokemon.gone()
                }
                is LoadingNextPageState -> {
                    bi.loader.gone()
                    showToast(getString(R.string.message_load_pokemon_str))
                }
                is SecondaryLoadingState -> {
                    bi.loader.visible()
                }
                is ErrorSecondaryState -> {
                    bi.loader.gone()
                    showToast(state.message)

                }
                else -> {}
            }
        }
        viewModel.pokemonListLiveData.observe(this) { pokemon ->
            pokemon?.let {
                bi.tvCount.text = "Total Pokemon " + pokemon.count
                pokemonAdapter.updateItems(pokemon.results)

            }
        }
        viewModel.pokemonLiveData.observe(this) { pokemon ->
            Log.e("Main", "pokeman update" + Gson().toJson(pokemon))
            pokemon?.url = "https://pokeapi.co/api/v2/pokemon/$id/"
            pokemonAdapter.updateItem(pokemon, position)
        }
    }

    private fun setUpViews() {
        baseContext?.let { ctx ->
            bi.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, _ ->
                Log.e("Mian", "nest called")
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    viewModel.loadMorePokemon()
                }

            }

            pokemonAdapter = PokemonAdapter() { pokemon, position ->

                this.position = position
                this.id = getIdFromUrl(pokemon.url!!) + 151
                viewModel.changePokemon(id)
            }

            pokemonAdapter.setHasStableIds(true)
            bi.rvPokemon.adapter = pokemonAdapter
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_day_night_mode -> {
                // Get new mode.
                val mode =
                    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_NO
                    ) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }

                // Change UI Mode
                AppCompatDelegate.setDefaultNightMode(mode)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}