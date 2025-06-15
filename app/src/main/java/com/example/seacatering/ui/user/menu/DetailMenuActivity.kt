package com.example.seacatering.ui.user.menu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityContactUsBinding
import com.example.seacatering.databinding.ActivityDashboardBinding
import com.example.seacatering.databinding.ActivityDetailMenuBinding

class DetailMenuActivity : AppCompatActivity() {

    private var _binding: ActivityDetailMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val pageTitle = getString(R.string.detail_menu)
        supportActionBar?.title = pageTitle

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}