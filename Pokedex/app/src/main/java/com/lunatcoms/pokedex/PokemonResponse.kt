package com.lunatcoms.pokedex

import com.google.gson.annotations.SerializedName

data class PokemonResponse(@SerializedName("results") val results: List<Pokemon>)

data class Pokemon(@SerializedName("name") val name: String, @SerializedName("url") val url: String)