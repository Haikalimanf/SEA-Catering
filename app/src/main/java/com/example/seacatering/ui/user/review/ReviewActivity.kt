package com.example.seacatering.ui.user.review

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityContactUsBinding
import com.example.seacatering.databinding.ActivityDashboardBinding
import com.example.seacatering.databinding.ActivityReviewBinding
import com.example.seacatering.utils.DialogUtil

class ReviewActivity : AppCompatActivity() {

    private var _binding: ActivityReviewBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val pageTitle = getString(R.string.review)
        supportActionBar?.title = pageTitle

        binding.btnPost.setOnClickListener {
            DialogUtil.showConfirmationDialog(
                context = this,
                title = "Post Review",
                message = "Are you sure you want to post review?",
                onConfirmed = {
                    finish()
                }
            )
        }

        binding.btnCancel.setOnClickListener {

        }
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