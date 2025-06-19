package com.example.seacatering.ui.user.review

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityContactUsBinding
import com.example.seacatering.databinding.ActivityDashboardBinding
import com.example.seacatering.databinding.ActivityReviewBinding
import com.example.seacatering.utils.DialogUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewActivity : AppCompatActivity() {

    private var _binding: ActivityReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel : ReviewViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.review)

        binding.btnPost.setOnClickListener {
            val reviewText = binding.edtReview.text.toString().trim()
            val rating = binding.ratingBar.rating.toInt()

            if (reviewText.isNotBlank() && rating > 0) {
                DialogUtil.showConfirmationDialog(
                    context = this,
                    title = "Post Review",
                    message = "Are you sure you want to post review?",
                    onConfirmed = {
                        viewModel.postReview(reviewText, rating)
                    }
                )
            } else {
                Toast.makeText(this, "Please write a review and select a rating", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.postReview.collect { result ->
                result?.let {
                    if (it.isSuccess) {
                        Toast.makeText(this@ReviewActivity, "Review posted successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@ReviewActivity, "Failed to post review", Toast.LENGTH_SHORT).show()
                    }
                    viewModel.resetResult()
                }
            }
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