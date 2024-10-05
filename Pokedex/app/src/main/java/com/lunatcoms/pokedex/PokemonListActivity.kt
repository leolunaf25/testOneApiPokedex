package com.lunatcoms.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.lunatcoms.pokedex.databinding.ActivityPokemonListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonListBinding

    private lateinit var adapter: PokemonAdapter

    private lateinit var generationName:String

    private var pokemonList: List<Pokemon> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        generationName = intent.getStringExtra("GENERATION_NAME") ?: ""
        binding.tvPokemonList.text = generationName

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
        initUI(offset,limit)
        initSearchView()
    }

    private fun initSearchView() {
        binding.searchvPokemon.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                // Filtrar la lista de PokemonItem usando el nombre
                val filteredPokemon = pokemonList.filter { it.name.contains(newText ?: "", ignoreCase = true) }

                // Actualizar la lista filtrada en el adapter
                adapter.updateList(filteredPokemon)
                return true
            }

        })
    }

    private fun initUI(start:Int, end:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIPokemon::class.java).getPokemon(end,start)
            //val pokemon = call.body()?.results?.map { it.name } ?: emptyList()
            //val pokemonUrl = call.body()?.results?.map { it.url} ?: emptyList()

            val pokemon = call.body()?.results?.map { Pokemon(it.name, it.url) } ?: emptyList()

            runOnUiThread {
                if (call.isSuccessful) {
                    pokemonList = pokemon
                    Log.i("Status", "Conexion realizada")
                    adapter = PokemonAdapter(pokemonList){pokeId -> navigateToPokemonData(pokeId)}
                    binding.rvPokemon.layoutManager = LinearLayoutManager(parent)
                    binding.rvPokemon.adapter = adapter

                } else {
                    Log.i("Status", "Conexion No realizada")
                }
            }
        }
    }

    private fun navigateToPokemonData(pokeId: String) {
        val intent = Intent(this, DataPokemonActivity::class.java)
        intent.putExtra("ID_POKEMON", pokeId)
        startActivity(intent)
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
