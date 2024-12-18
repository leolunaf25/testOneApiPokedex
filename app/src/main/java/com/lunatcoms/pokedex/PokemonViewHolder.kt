package com.lunatcoms.pokedex

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lunatcoms.pokedex.databinding.ItemPokemonBinding
import com.squareup.picasso.Picasso

class PokemonViewHolder(view: View, private val onItemSelected: (String) -> Unit) :
    RecyclerView.ViewHolder(view) {

    private val binding = ItemPokemonBinding.bind(view)

    fun bind(pokemon: String, pokemonUrl: String) {
        binding.tvPokemonName.text = pokemon.replaceFirstChar { it.uppercase() }
        binding.tvPokemonId.text = pokemonUrl

        Picasso.get()
            //.load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonUrl.png")
            .load(itemView.context.getString(R.string.urlImageSprite,pokemonUrl))
            .fit()
            .error(R.drawable.ic_btn_intro)
            .into(binding.ivPokemon)
        binding.btnPokemon.setOnClickListener { onItemSelected(pokemonUrl) }
    }

}