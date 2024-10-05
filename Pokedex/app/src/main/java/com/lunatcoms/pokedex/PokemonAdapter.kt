package com.lunatcoms.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PokemonAdapter(
    private var pokemon: List<Pokemon>,
    private val onItemSelected: (String) -> Unit
) : RecyclerView.Adapter<PokemonViewHolder>() {

    fun updateList(list: List<Pokemon>){
        pokemon = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PokemonViewHolder(layoutInflater.inflate(R.layout.item_pokemon, parent, false), onItemSelected)
    }

    override fun getItemCount(): Int = pokemon.size

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val item = pokemon[position]
        val itemId = """.*/(\d+)/""".toRegex().find(item.url)?.groupValues?.get(1) ?: ""

        holder.bind(item.name,itemId)
    }
}