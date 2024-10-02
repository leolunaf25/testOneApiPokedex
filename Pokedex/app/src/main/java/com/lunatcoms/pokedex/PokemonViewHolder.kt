package com.lunatcoms.pokedex

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lunatcoms.pokedex.databinding.ItemPokemonBinding
import com.squareup.picasso.Picasso

class PokemonViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    private val binding = ItemPokemonBinding.bind(view)

    fun bind(pokemon: String, pokemonUrl: String) {
        binding.tvPokemonName.text = pokemon
        binding.tvPokemonId.text = pokemonUrl

        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonUrl.png")
            .fit()
            .into(binding.ivPokemon)

        //binding.btnGeneration.setOnClickListener { onItemSelected(position.toString()) }
    }

}