package com.example.seacatering.ui.user.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityContactUsBinding
import com.example.seacatering.databinding.ActivityDashboardBinding
import com.example.seacatering.ui.user.dashboard.bottomsheet.PauseSubscriptionBottomSheet
import com.example.seacatering.utils.DialogUtil
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private var _binding: ActivityDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val pageTitle = getString(R.string.dashboard)
        supportActionBar?.title = pageTitle

        binding.btnPause.setOnClickListener {
            val bottomSheet = PauseSubscriptionBottomSheet()
            bottomSheet.show(supportFragmentManager, PauseSubscriptionBottomSheet::class.java.simpleName)
        }

        binding.btnCancelSubscription.setOnClickListener {
            DialogUtil.showConfirmationDialog(
                context = this,
                title = "Cancel Subscription",
                message = "Are you sure you want to cancel subscription?",
                onConfirmed = {
                    Toast.makeText(this, "Subscription canceled", Toast.LENGTH_SHORT).show()
                }
            )
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