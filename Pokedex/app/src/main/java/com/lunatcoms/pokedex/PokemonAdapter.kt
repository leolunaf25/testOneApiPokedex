package com.lunatcoms.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lunatcoms.pokedex.R
import com.lunatcoms.pokedex.PokemonListActivity

class PokemonAdapter(
    private val pokemon: List<String>,
    private val pokemonUrl: List<String>
) : RecyclerView.Adapter<PokemonViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PokemonViewHolder(layoutInflater.inflate(R.layout.item_pokemon, parent, false))
    }

    override fun getItemCount(): Int = pokemon.size


    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val itemId = """.*/(\d+)/""".toRegex().find(pokemonUrl[position])?.groupValues?.get(1) ?: ""
        val item = pokemon[position]

        holder.bind(item,itemId)
    }
}