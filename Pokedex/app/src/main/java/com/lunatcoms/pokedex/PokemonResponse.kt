package com.lunatcoms.pokedex

import com.google.gson.annotations.SerializedName

data class PokemonResponse(@SerializedName("results") val results: List<Pokemon>)
data class Pokemon(@SerializedName("name") val name: String, @SerializedName("url") val url: String)

data class PokemonDataResponse(
    @SerializedName("name") val name: String,
    @SerializedName("base_experience") val experience: String,
    @SerializedName("height") val height: String,
    @SerializedName("weight") val weight: String,
    @SerializedName("types") val types: List<TypeSlot>
)
data class TypeSlot(@SerializedName("type") val type: TypeRes)
data class TypeRes(@SerializedName("url") val name: String)