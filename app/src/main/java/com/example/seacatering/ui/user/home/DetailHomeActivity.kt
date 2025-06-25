package com.example.seacatering.ui.user.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityDetailHomeBinding

class DetailHomeActivity : AppCompatActivity() {

    private var _binding: ActivityDetailHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val pageTitle = getString(R.string.detail_Home)
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