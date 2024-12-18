package com.lunatcoms.pokedex.menuGenerations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lunatcoms.pokedex.R

class GenerationAdapter(
    private val texts: List<String>,
    private val onItemSelected: (String) -> Unit
) :
    RecyclerView.Adapter<GenerationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenerationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return GenerationViewHolder(layoutInflater.inflate(R.layout.item_generation, parent, false),onItemSelected)
    }

    override fun getItemCount(): Int = texts.size

    override fun onBindViewHolder(holder: GenerationViewHolder, position: Int) {
        val item = texts[position]
        holder.bind(item,position)
    }
}