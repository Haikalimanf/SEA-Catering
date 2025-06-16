package com.example.seacatering.ui.admin.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seacatering.databinding.ActivityDashboardAdminBinding
class DashboardAdminActivity : AppCompatActivity() {

    private var _binding: ActivityDashboardAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}