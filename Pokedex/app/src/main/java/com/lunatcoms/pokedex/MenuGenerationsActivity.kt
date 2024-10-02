package com.lunatcoms.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lunatcoms.pokedex.databinding.ActivityMenuGenerationsBinding
import com.lunatcoms.pokedex.menuGenerations.GenerationAdapter

class MenuGenerationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuGenerationsBinding
    private lateinit var adapter: GenerationAdapter

    private val generaciones by lazy {
        resources.getStringArray(R.array.generaciones).toList()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuGenerationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

    }
    private fun initRecyclerView() {
        adapter = GenerationAdapter(generaciones) {generation -> navigateToPokemonList(generation)}
        binding.rvGenerations.layoutManager = LinearLayoutManager(this)
        binding.rvGenerations.adapter = adapter
    }

    private fun navigateToPokemonList(generation: String){
        val intent = Intent(this, PokemonListActivity::class.java)
        intent.putExtra("GENERATION_NAME", generation)
        startActivity(intent)
    }
}