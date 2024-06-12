package com.example.trashify.ui.main

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trashify.ViewModelFactory
import com.example.trashify.data.preference.UserModel
import com.example.trashify.data.reponse.ListStoryItem
import com.example.trashify.databinding.ActivityMainBinding
import com.example.trashify.ui.adaptasi.StoryAdapter
import com.example.trashify.ui.addstory.AddStoryActivity
import com.example.trashify.ui.login.LoginActivity
import com.example.trashify.ui.profile.AboutActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        viewModel.getSession().observe(this) { user ->
            runBlocking { delay(1500) }
            if (user.token.isNotEmpty() && user.token != "") {
                token = user.token
                setupView()
                setupAction(user)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        playAnimation()
    }
    private fun playAnimation() {
        val item_story = ObjectAnimator.ofFloat(binding.rvListStories, View.ALPHA, 1f).setDuration(100)
        item_story.start()
    }

    private fun setupAction(user: UserModel) {
        binding.About.setOnClickListener {
            val intent = Intent(this@MainActivity, AboutActivity::class.java)
            intent.putExtra("name", user.name)
            intent.putExtra("email", user.email)
            startActivity(intent)
        }

        binding.Main.setOnClickListener {
            // This button will have no effects
        }

        binding.fab.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
        }

        lifecycleScope.launch {
            viewModel.getAllStoriesItem.collectLatest {
                setAllStoriesList(it)
            }
        }
        viewModel.getAllStories(token)
    }

    private fun setAllStoriesList(items: List<ListStoryItem>) {
        binding.rvListStories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = StoryAdapter().apply { submitList(items) }
            setHasFixedSize(true)
        }
        if (items.isNotEmpty()) showToast("Result ${items.size}")
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        viewModel.isLoading.observe(this@MainActivity) {
            showLoading(it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}