package com.lunatcoms.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lunatcoms.pokedex.databinding.ActivityDataPokemonBinding

class DataPokemonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataPokemonBinding

    private lateinit var pokeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDataPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pokeId = intent.getStringExtra("ID_POKEMON") ?: "Error"

        binding.tvIdPokemon.text = pokeId
    }
}