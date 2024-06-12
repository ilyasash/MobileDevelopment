package com.example.trashify.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.trashify.ViewModelFactory
import com.example.trashify.data.database.ProfilePicture
import com.example.trashify.databinding.ActivityAboutBinding
import com.example.trashify.ui.addstory.AddStoryActivity
import com.example.trashify.ui.main.MainActivity
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {
    private const val PROFILE_PICTURE = "profile_picture.jpg"

    fun copyUriToInternalStorage(context: Context, uri: Uri): Uri? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, PROFILE_PICTURE)
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    private val viewModel by viewModels<AboutViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupActionBar()
        setupIntentData()
        setupClickListeners()
        observeProfilePicture()
    }

    private fun setupBinding() {
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = "About"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupIntentData() {
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")

        Log.d("AboutActivity", "Received name: $name, email: $email")

        binding.aboutName.text = name ?: "No Name"
        binding.aboutGmail.text = email ?: "No Email"
    }

    private fun setupClickListeners() {
        binding.logout.setOnClickListener {
            viewModel.logout()
        }

        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        binding.Main.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.addProfilePicture.setOnClickListener {
            openGalleryForImage()
        }
    }

    private fun observeProfilePicture() {
        viewModel.profilePicture.observe(this) { profilePicture ->
            profilePicture?.let {
                binding.profileImageView.setImageURI(Uri.parse(profilePicture.pictureUri))
            }
        }
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    lifecycleScope.launch {
                        val copiedUri = FileUtils.copyUriToInternalStorage(this@AboutActivity, uri)
                        copiedUri?.let { savedUri ->
                            viewModel.insertProfilePicture(ProfilePicture(0, savedUri.toString()))
                        }
                    }
                }
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        galleryLauncher.launch(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
