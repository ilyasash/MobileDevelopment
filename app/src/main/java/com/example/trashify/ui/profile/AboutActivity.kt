package com.example.trashify.ui.profile

import android.app.Activity
import android.app.AlertDialog
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
import com.example.trashify.R
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

        // removing the shadow effect on the BottomNavigationView
        binding.bottomNavigationView.background = null

        // making the placeholder menu item unclickable
        binding.bottomNavigationView.menu.getItem(1).isEnabled = false

        // Set the About item as selected
        binding.bottomNavigationView.selectedItemId = R.id.About

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
            AlertDialog.Builder(this)
                .setTitle("Log Out?")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log Out") { _, _ ->
                    viewModel.logout()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Main -> {
                    val intent = Intent(this@AboutActivity, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.About -> {
                    // this button will have no effect
                    true
                }
                else -> false
            }
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
