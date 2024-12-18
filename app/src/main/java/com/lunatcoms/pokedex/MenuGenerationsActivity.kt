package com.lunatcoms.pokedex

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.lunatcoms.pokedex.databinding.ActivityMenuGenerationsBinding
import com.lunatcoms.pokedex.menuGenerations.GenerationAdapter

class MenuGenerationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuGenerationsBinding
    private lateinit var adapter: GenerationAdapter

    private val generations by lazy {
        resources.getStringArray(R.array.generaciones).toList()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuGenerationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()

    }
    private fun initRecyclerView() {
        adapter = GenerationAdapter(generations) { generation -> navigateToPokemonList(generation) }
        binding.rvGenerations.layoutManager = LinearLayoutManager(this)
        binding.rvGenerations.adapter = adapter
    }

    private fun navigateToPokemonList(generation: String) {

        if (isNetworkAvailable(this)) {
            val intent = Intent(this, PokemonListActivity::class.java)
            intent.putExtra(ApiConstants.GENERATION_NAME, generation)
            startActivity(intent)
        } else {
            showError(getString(R.string.connection_Error_one))
        }
    }
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


}