package com.lunatcoms.pokedex

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.palette.graphics.Palette
import com.lunatcoms.pokedex.databinding.ActivityDataPokemonBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception
import com.squareup.picasso.Target


class DataPokemonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataPokemonBinding

    private lateinit var pokeId: String

    private lateinit var imageViews: List<ImageView>

    private val imageNormal = R.string.urlImageNormal
    private val imageShiny = R.string.urlImageShiny


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDataPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pokeId = intent.getStringExtra(ApiConstants.ID_POKEMON) ?:ApiConstants.ERROR

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
                val dataPokemon = getRetrofit().create(APIPokemon::class.java).getDataPokemon(pokeID)
                val name = dataPokemon.body()?.name ?: getString(R.string.default_value)
                val height = dataPokemon.body()?.height ?: 0
                val weight = dataPokemon.body()?.weight ?: 0
                val experience = dataPokemon.body()?.experience ?: getString(R.string.default_value)
                val type = dataPokemon.body()?.types?.map { it.type.name } ?: emptyList()

                runOnUiThread {
                    if (dataPokemon.isSuccessful) {
                        binding.tvNamePokemon.text = name.uppercase()

                        if (pokeID=="7"){
                            binding.tvNamePokemon.text = getString(R.string.easter_egg)
                        }

                        binding.tvHeight.text = getString(R.string.heightUnit_format, (height/10F).toString())
                        binding.tvWeight.text = getString(R.string.weightUnit_format, (weight/10F).toString())

                        binding.tvExperience.text = experience
                        imageViews = listOf(binding.ivType1, binding.ivType2)

                        if (type.count()==1){binding.ivType2.visibility = View.GONE}
                        for (i in type.indices) {

                            //val aux = """.*/(\\d+)/""".toRegex().find(type[i])?.groupValues?.get(1) ?: getString(R.string.default_value)
                            val aux = getString(R.string.url_regex).toRegex().find(type[i])?.groupValues?.get(1) ?: getString(R.string.default_value)

                            Picasso.get()
                                .load(getString(R.string.urlImageType,aux))
                                .error(R.drawable.ic_btnintro)
                                .into(imageViews[i])
                        }

                    } else {
                        showError(getString(R.string.connection_Error))
                    }
                }

            } catch (e: IOException) {
                runOnUiThread {
                    showError(getString(R.string.connection_Error))
                }
            }

        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun loadImage(imageUrl: Int, pokeID: String) {
        binding.pbImageMain.visibility = View.VISIBLE
        Picasso.get()
            .load(getString(imageUrl,pokeID))
            .error(R.drawable.ic_btnintro)
            .into(binding.ivMainPoke, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    binding.pbImageMain.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    binding.pbImageMain.visibility = View.GONE
                    showError(getString(R.string.connection_Error_one))
                }
            })

        Picasso.get().load(getString(imageUrl,pokeID)).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                // Usa Palette para extraer los colores
                Palette.from(bitmap).generate { palette ->
                    palette?.let {
                        // Obtener los colores comunes
                        val dominantColor = it.getDominantColor(Color.BLACK) // Color dominante

                        // Aplicar el color dominante como fondo de un layout
                        binding.viewMain.setBackgroundColor(dominantColor)
                        window.statusBarColor = dominantColor
                    }
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                // Manejo de errores
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                // Mostrar un placeholder optional
            }
        })

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}