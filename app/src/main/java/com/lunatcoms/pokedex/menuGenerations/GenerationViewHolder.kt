package com.lunatcoms.pokedex.menuGenerations

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lunatcoms.pokedex.R
import com.lunatcoms.pokedex.databinding.ItemGenerationBinding
import com.squareup.picasso.Picasso


class GenerationViewHolder(view: View, private val onItemSelected: (String) -> Unit) :
    RecyclerView.ViewHolder(view) {

    private val binding = ItemGenerationBinding.bind(view)

    private lateinit var imageViews: List<ImageView>

    fun bind(generation: String, position: Int) {
        binding.tvGeneration.text = generation
        binding.btnGeneration.setOnClickListener { onItemSelected(position.toString()) }
        imageViews = listOf(binding.ivR1, binding.ivR2, binding.ivR3)

        val startPoints = itemView.context.resources.getIntArray(R.array.StartPoints).toList()
        val aux = startPoints.getOrNull(position) ?: return // Avoid indices out of range

        for (i in imageViews.indices) {
            Picasso.get()
                //.load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${aux + i * 3}.png")
                .load(itemView.context.getString(R.string.urlImageNormal,(aux + i * 3).toString()))
                .fit()
                .error(R.drawable.ic_btn_intro)
                .into(imageViews[i])
        }

    }

}