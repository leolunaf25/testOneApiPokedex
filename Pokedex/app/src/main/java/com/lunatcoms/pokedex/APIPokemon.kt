package com.lunatcoms.pokedex

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface APIPokemon {
    @GET("pokemon")
    suspend fun getPokemon(@Query("limit") limit:Int, @Query("offset") offset:Int): Response<PokemonResponse>
}