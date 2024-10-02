package com.lunatcoms.pokedex.menuGenerations

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lunatcoms.pokedex.databinding.ItemGenerationBinding

class GenerationViewHolder(view: View, private val onItemSelected: (String) -> Unit) :
    RecyclerView.ViewHolder(view) {

    private val binding = ItemGenerationBinding.bind(view)

    fun bind(generation: String, position: Int) {
        binding.tvGeneration.text = generation
        binding.btnGeneration.setOnClickListener { onItemSelected(position.toString()) }

    }

}