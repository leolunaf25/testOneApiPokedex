package com.lunatcoms.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.lunatcoms.pokedex.databinding.ActivityPokemonListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class PokemonListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonListBinding

    private lateinit var adapter: PokemonAdapter

    private lateinit var generationName: String

    private var pokemonList: List<Pokemon> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        generationName = intent.getStringExtra(ApiConstants.GENERATION_NAME) ?: getString(R.string.default_value)

        val (offset, limit) = when (generationName.toInt()) {
            0 -> 0 to 151
            1 -> 151 to 100
            2 -> 251 to 135
            3 -> 386 to 107
            4 -> 494 to 155
            5 -> 649 to 72
            6 -> 721 to 88
            7 -> 809 to 96
            8 -> 905 to 120
            else -> 0 to 0
        }
        initUI(offset, limit)
        initSearchView()
    }

    private fun initSearchView() {
        binding.searchvPokemon.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filtrar la lista de PokemonItem usando el nombre
                val filteredPokemon =
                    pokemonList.filter { it.name.contains(newText ?: getString(R.string.default_value), ignoreCase = true) }

                // Actualizar la lista filtrada en el adapter
                adapter.updateList(filteredPokemon)
                return true
            }

        })
    }

    private fun initUI(start: Int, end: Int) {
        CoroutineScope(Dispatchers.IO).launch {

            //Manejo de errores
            try {
                val call = getRetrofit().create(APIPokemon::class.java).getPokemon(end, start)
                //Código implementado al inicio de la práctica.
                //val pokemon = call.body()?.results?.map { it.name } ?: emptyList()
                //val pokemonUrl = call.body()?.results?.map { it.url} ?: emptyList()

                val pokemon = call.body()?.results?.map { Pokemon(it.name, it.url) } ?: emptyList()

                runOnUiThread {
                    if (call.isSuccessful) {
                        pokemonList = pokemon
                        adapter = PokemonAdapter(pokemonList) { pokeId -> navigateToPokemonData(pokeId) }
                        binding.rvPokemon.layoutManager = LinearLayoutManager(parent)
                        binding.rvPokemon.adapter = adapter

                    } else {
                        runOnUiThread {
                            showError(getString(R.string.connection_Error_one))
                        }
                    }
                }

            } catch (e: IOException) {
                runOnUiThread {
                    showError(getString(R.string.connection_Error))
                }
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun navigateToPokemonData(pokeId: String) {
        val isNet = MenuGenerationsActivity()
        if (isNet.isNetworkAvailable(this)) {
            val intent = Intent(this, DataPokemonActivity::class.java)
            intent.putExtra(ApiConstants.ID_POKEMON, pokeId)
            startActivity(intent)
        } else {
            showError(getString(R.string.connection_Error))
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
