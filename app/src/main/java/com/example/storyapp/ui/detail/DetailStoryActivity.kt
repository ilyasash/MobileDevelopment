package com.example.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.example.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var photoUrl: String
    private lateinit var name: String
    private lateinit var description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        deklarasiExtra()
        setupAction()
    }

    private fun deklarasiExtra() {
        val def_PhotoUrl = "N/A" // URL foto default jika photoUrl tidak tersedia
        val def_Name = "N/A" // Nama default jika name tidak tersedia
        val def_Description = "N/A" // Deskripsi default jika description tidak tersedia

        photoUrl = intent.getStringExtra(EXTRA_PHOTO_URL) ?: def_PhotoUrl
        name = intent.getStringExtra(EXTRA_NAME) ?: def_Name
        description = intent.getStringExtra(EXTRA_DESC) ?: def_Description
    }

    private fun setupAction() {
        binding.apply {
            photoUrlImageView.load(photoUrl) // Memuat foto ke ImageView
            textName.text = name // Menampilkan nama pada TextView
            textDescriptions.text = description // Menampilkan deskripsi pada TextView
        }
    }

    companion object {
        const val EXTRA_PHOTO_URL = "extra_photourl"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_description"
    }
}
