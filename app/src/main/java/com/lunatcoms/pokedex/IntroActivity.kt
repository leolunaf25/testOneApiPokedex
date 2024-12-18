package com.lunatcoms.pokedex

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lunatcoms.pokedex.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        mediaPlayer = MediaPlayer.create(this, R.raw.pikachu)

        setContentView(binding.root)

        binding.btnInit.setOnClickListener {
            navigateToMenu()
            startSound()
        }
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuGenerationsActivity::class.java)
        startActivity(intent)
    }

    private fun startSound() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.seekTo(0) // Reset to original position
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}