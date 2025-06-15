package com.example.seacatering.ui.user.profile

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityContactUsBinding
import com.example.seacatering.databinding.ActivityDashboardBinding
import com.example.seacatering.databinding.ActivityProfileBinding
import com.example.seacatering.utils.ImageUtils

class ProfileActivity : AppCompatActivity() {

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val pageTitle = getString(R.string.profile)
        supportActionBar?.title = pageTitle

        binding.btnChangePhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
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