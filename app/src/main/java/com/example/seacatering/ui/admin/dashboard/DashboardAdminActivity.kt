package com.example.seacatering.ui.admin.dashboard

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.seacatering.databinding.ActivityDashboardAdminBinding
import com.example.seacatering.model.DataAnalyticsResult
import com.example.seacatering.ui.auth.login.LoginActivity
import com.example.seacatering.ui.user.profile.ProfileViewModel
import com.example.seacatering.utils.FormatRupiah.formatRupiah
import com.example.seacatering.utils.PdfReportGenerator
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@AndroidEntryPoint
class DashboardAdminActivity : AppCompatActivity() {

    private var _binding: ActivityDashboardAdminBinding? = null
    private val binding get() = _binding!!

    private val dashboardViewModel: AdminDashboardViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    private var currentStartDate: Timestamp? = null
    private var currentEndDate: Timestamp? = null

    private var latestAnalyticsResult: DataAnalyticsResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDateRangeSelector()
        triggerSetDate()
        logOut()
        createReport()
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
                "Today" -> Pair(now, now)
                "Last 7 days" -> Pair(now.minusDays(6), now)
                "Last 30 days" -> Pair(now.minusDays(29), now)
                "This month" -> Pair(now.withDayOfMonth(1), now)
                "This year" -> Pair(now.withDayOfYear(1), now)
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
        currentStartDate = start
        currentEndDate = end

        dashboardViewModel.fetchDashboardAnalytics(start, end)
        lifecycleScope.launch {
            dashboardViewModel.dataAnalytics.collect { result ->
                if (result != null) {
                    latestAnalyticsResult = result
                    binding.newSubscriptions.text = result.newSubscriptions.toString()
                    binding.totalRevenue.text = formatRupiah(result.recurringRevenue)
                    binding.totalActiveSubscribers.text = result.subscriptionActive.toString()
                    binding.reactivatedSubscriptions.text = result.reactivations.toString()
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

    fun createReport() {
        binding.btnCreateReport.setOnClickListener {
            val start = currentStartDate
            val end = currentEndDate
            val data = latestAnalyticsResult

            if (start != null && end != null && data != null) {
                val file = PdfReportGenerator.generateSubscriptionReport(this, start, end, data)
                if (file != null) {
                    PdfReportGenerator.openPdf(this, file)
                } else {
                    Toast.makeText(this, "Failed to create PDF", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Data is not yet ready for reporting", Toast.LENGTH_SHORT).show()
            }
        }

    }

    companion object {
        private val DATE_OPTIONS = listOf(
            "Today",
            "Last 7 days",
            "Last 30 days",
            "This month",
            "This year"
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}