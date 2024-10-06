package com.lunatcoms.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.lunatcoms.pokedex.databinding.ActivityDataPokemonBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception

class DataPokemonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataPokemonBinding

    private lateinit var pokeId: String

    private lateinit var imageViews: List<ImageView>

    private val imageNormal =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
    private val imageShiny =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/shiny/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDataPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pokeId = intent.getStringExtra("ID_POKEMON") ?: "Error"

        initUI(pokeId)

        loadImage(imageNormal, pokeId)

    }

    private fun initUI(pokeID: String) {

        binding.swShiny.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                loadImage(imageShiny, pokeID)
            } else {
                loadImage(imageNormal, pokeID)
            }
        }


        CoroutineScope(Dispatchers.IO).launch {

            try {
                val dataPokemon =
                    getRetrofit().create(APIPokemon::class.java).getDataPokemon(pokeID)
                val name = dataPokemon.body()?.name ?: ""
                val height = dataPokemon.body()?.height ?: ""
                val weight = dataPokemon.body()?.weight ?: ""
                val experience = dataPokemon.body()?.experience ?: ""
                val type = dataPokemon.body()?.types?.map { it.type.name } ?: emptyList()

                runOnUiThread {
                    if (dataPokemon.isSuccessful) {
                        binding.tvNamePokemon.text = name.uppercase()

                        if (pokeID=="7"){
                            binding.tvNamePokemon.text = "Vamo a calmarno"
                        }

                        binding.tvHeight.text = height
                        binding.tvWeight.text = weight
                        binding.tvExperience.text = experience
                        imageViews = listOf(binding.ivType1, binding.ivType2)

                        if (type.count()==1){binding.ivType2.visibility = View.GONE}
                        for (i in type.indices) {

                            val aux =
                                """.*/(\d+)/""".toRegex().find(type[i])?.groupValues?.get(1) ?: ""

                            Picasso.get()
                                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/types/generation-ix/scarlet-violet/$aux.png")
                                .error(R.drawable.ic_btnintro)
                                .into(imageViews[i])
                        }

                    } else {

                    }
                }

            } catch (e: IOException) {
                runOnUiThread {
                    showError("No se pudo realizar la conexión. Verifica tu conexión a internet.")
                }
            }

        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun loadImage(imageUrl: String, pokeID: String) {
        binding.pbImageMain.visibility = View.VISIBLE
        Picasso.get()
            .load("$imageUrl$pokeID.png")
            .error(R.drawable.ic_btnintro)
            .into(binding.ivMainPoke, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    binding.pbImageMain.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    binding.pbImageMain.visibility = View.GONE
                    showError("Error de conexión")
                }

            })

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}