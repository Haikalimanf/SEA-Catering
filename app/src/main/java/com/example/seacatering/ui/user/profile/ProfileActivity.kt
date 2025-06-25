package com.example.seacatering.ui.user.profile

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityProfileBinding
import com.example.seacatering.ui.auth.login.LoginActivity
import com.example.seacatering.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val pageTitle = getString(R.string.profile)
        supportActionBar?.title = pageTitle

        changeImage()
        fetchDataUser()
        changeDataProfile()
        logOut()
    }

    private fun fetchDataUser() {
        viewModel.fetchCurrentUser()
        lifecycleScope.launchWhenStarted {
            viewModel.userState.collect { user ->
                if (user != null) {
                    binding.edtUsername.setText(user.name)
                    binding.edtEmail.setText(user.email)
                    binding.edtAddres.setText(user.address)

                    val defaultUrl = getString(R.string.profile_image_default)

                    val imageUrl = user.imageUri?.takeIf { it.isNotEmpty() } ?: defaultUrl

                    Glide.with(this@ProfileActivity)
                        .load(imageUrl)
                        .circleCrop()
                        .into(binding.imgProfile)
                }
            }
        }
    }

    private fun changeDataProfile() {
        binding.btnSave.setOnClickListener {
            val newName = binding.edtUsername.text.toString()
            val newAddress = binding.edtAddres.text.toString()

            if (newName.isNotBlank() && newAddress.isNotBlank()) {
                AlertDialog.Builder(this)
                    .setTitle("Update Profile")
                    .setMessage("Are you sure you want to change the profile?")
                    .setPositiveButton("Yes") { _, _ ->
                        lifecycleScope.launch {
                            viewModel.updateUserProfile(newName, newAddress)
                            Toast.makeText(this@ProfileActivity, "Profile updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("No", null)
                    .show()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeImage() {
        binding.btnChangePhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun logOut() {
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to Log Out?")
                .setPositiveButton("Yes") { _, _ ->
                    lifecycleScope.launch {
                        viewModel.logout()
                        Toast.makeText(this@ProfileActivity, "Logout", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.logoutState.collect { loggedOut ->
                if (loggedOut) {
                    startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val copiedUri = ImageUtils.copyImageToInternalStorage(this, it)
            copiedUri?.let { safeUri ->
                selectedImageUri = safeUri
                Glide.with(this)
                    .load(safeUri)
                    .circleCrop()
                    .into(binding.imgProfile)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}