package com.example.seacatering.ui.admin.dashboard

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.seacatering.databinding.ActivityDashboardAdminBinding
import com.example.seacatering.ui.auth.login.LoginActivity
import com.example.seacatering.ui.user.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.ZoneId
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class DashboardAdminActivity : AppCompatActivity() {

    private var _binding: ActivityDashboardAdminBinding? = null
    private val binding get() = _binding!!

    private val dashboardViewModel: AdminDashboardViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDateRangeSelector()
        triggerSetDate()
        logOut()
    }

    private fun triggerSetDate() {
        val today = LocalDate.now()
        val startDateTime = today.atStartOfDay()
        val endDateTime = today.atTime(23, 59, 59)
        val startTimestamp = Timestamp(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()))
        val endTimestamp = Timestamp(Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()))
        fetchDataSubcription(startTimestamp, endTimestamp)

        binding.dateRangeSelector.setText(DATE_OPTIONS[0], false)
    }

    private fun setupDateRangeSelector() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, DATE_OPTIONS)
        binding.dateRangeSelector.setAdapter(adapter)
        binding.dateRangeSelector.setOnClickListener {
            binding.dateRangeSelector.showDropDown()
        }

        binding.dateRangeSelector.setOnItemClickListener { parent, _, position, _ ->
            val selectedOption = parent.getItemAtPosition(position) as String
            val now = LocalDate.now()

            val (startDate, endDate) = when (selectedOption) {
                "Hari ini" -> Pair(now, now)
                "7 hari terakhir" -> Pair(now.minusDays(6), now)
                "30 hari terakhir" -> Pair(now.minusDays(29), now)
                "Bulan ini" -> Pair(now.withDayOfMonth(1), now)
                "Tahun ini" -> Pair(now.withDayOfYear(1), now)
                else -> Pair(now, now)
            }

            val startDateTime = startDate.atStartOfDay()
            val endDateTime = endDate.atTime(23, 59, 59)

            val startTimestamp = Timestamp(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()))
            val endTimestamp = Timestamp(Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()))

            fetchDataSubcription(startTimestamp, endTimestamp)
        }
    }

    private fun fetchDataSubcription(start: Timestamp, end: Timestamp) {
        dashboardViewModel.fetchDashboardAnalytics(start, end)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dashboardViewModel.dataAnalytics.collect { result ->
                    if (result != null) {
                        binding.newSubscriptions.text = result.newSubscriptions.toString()
                        binding.totalRevenue.text = result.recurringRevenue
                        binding.totalActiveSubscribers.text = result.subscriptionActive.toString()
                        binding.reactivatedSubscriptions.text = result.reactivations.toString()
                    }
                }
            }
        }
    }

    private fun logOut() {
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to Log Out?")
                .setPositiveButton("Yes") { _, _ ->
                    lifecycleScope.launch {
                        profileViewModel.logout()
                        Toast.makeText(this@DashboardAdminActivity, "Logout", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        lifecycleScope.launchWhenStarted {
            profileViewModel.logoutState.collect { loggedOut ->
                if (loggedOut) {
                    startActivity(Intent(this@DashboardAdminActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }

    companion object {
        private val DATE_OPTIONS = listOf(
            "Hari ini",
            "7 hari terakhir",
            "30 hari terakhir",
            "Bulan ini",
            "Tahun ini"
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}