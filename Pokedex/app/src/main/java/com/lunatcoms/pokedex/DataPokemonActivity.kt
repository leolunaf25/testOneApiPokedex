package com.lunatcoms.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.lunatcoms.pokedex.databinding.ActivityDataPokemonBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataPokemonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataPokemonBinding

    private lateinit var pokeId: String

    private lateinit var imageViews: List<ImageView>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDataPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pokeId = intent.getStringExtra("ID_POKEMON") ?: "Error"

        initUI(pokeId)
    }

    private fun initUI(pokeID:String) {

        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokeID.png")
            .into(binding.ivMainPoke)

        CoroutineScope(Dispatchers.IO).launch{
            val dataPokemon = getRetrofit().create(APIPokemon::class.java).getDataPokemon(pokeID)

            val name = dataPokemon.body()?.name ?: ""
            val height = dataPokemon.body()?.height ?: ""
            val weight = dataPokemon.body()?.weight ?: ""
            val experience = dataPokemon.body()?.experience ?: ""
            val type = dataPokemon.body()?.types?.map { it.type.name } ?: emptyList()

            runOnUiThread{
                binding.tvNamePokemon.text = name.uppercase()
                binding.tvHeight.text = height
                binding.tvWeight.text = weight
                binding.tvExperience.text = experience
                imageViews = listOf(binding.ivType1,binding.ivType2)

                for (i in type.indices){
                    Log.i("Tipo$i", """.*/(\d+)/""".toRegex().find(type[i])?.groupValues?.get(1) ?: "")
                    val aux = """.*/(\d+)/""".toRegex().find(type[i])?.groupValues?.get(1) ?: ""
                    Picasso.get()
                        .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/types/generation-ix/scarlet-violet/$aux.png")
                        .fit()
                        .into(imageViews[i])
                }

            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}