package com.example.seacatering.ui.user.dashboard

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityDashboardBinding
import com.example.seacatering.model.enums.SubscriptionStatus
import com.example.seacatering.ui.user.dashboard.bottomsheet.PauseSubscriptionBottomSheet
import com.example.seacatering.ui.user.menu.MenuViewModel
import com.example.seacatering.ui.user.profile.ProfileViewModel
import com.example.seacatering.utils.DialogUtil
import com.example.seacatering.utils.FormatRupiah.formatRupiah
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private var _binding: ActivityDashboardBinding? = null
    private val binding get() = _binding!!

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val menuViewModel: MenuViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val pageTitle = getString(R.string.dashboard)
        supportActionBar?.title = pageTitle

        fetchDataSubcription()
        fetchDataUser()
        fetchImageMenu()
        pauseSubscription()
        cancelSubscription()
    }

    private fun fetchImageMenu() {
        lifecycleScope.launch {
            combine(
                menuViewModel.mealPlans,
                dashboardViewModel.subscriptionData
            ) { mealPlans, subscription ->
                val planTypeName = subscription?.plan_type_name
                val matchedMeal = mealPlans.find { it.name == planTypeName }
                matchedMeal
            }.collect { matchedMeal ->
                matchedMeal?.let {
                    Glide.with(this@DashboardActivity)
                        .load(it.imageUri)
                        .into(binding.imgMenuDashboard)
                }
            }
        }
    }

    private fun fetchDataUser() {
        profileViewModel.fetchCurrentUser()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.userState.collect { user ->
                    user?.let {
                        binding.greetingUser.text = getString(R.string.greeting, it.name)
                    }
                }
            }
        }
    }

    private fun fetchDataSubcription() {
        dashboardViewModel.fetchUserSubscription()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dashboardViewModel.subscriptionData.collect { data ->
                    if (data == null || data.status == SubscriptionStatus.CANCELED) {
                        binding.subscriptionContent.visibility = View.GONE
                        binding.emptySubscriptionText.visibility = View.VISIBLE
                    } else {
                        binding.subscriptionContent.visibility = View.VISIBLE
                        binding.emptySubscriptionText.visibility = View.GONE
                        binding.txtPackageName.text = data.plan_type_name
                        binding.txtSubscriptionStatus.text = data.status.name
                        binding.txtMealType.text = data.meal_plan.joinToString(", ")
                        binding.txtDeliveryDays.text = data.delivery_days.joinToString(", ")
                        val formattedDate = data.end_date.toDate().let { date ->
                            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                            sdf.format(date)
                        }
                        binding.txtNextRenewalDate.text = formattedDate
                        binding.txtTotalPrice.text = formatRupiah(data.total_price)
                    }
                }
            }
        }
    }


    private fun cancelSubscription() {
        binding.btnCancelSubscription.setOnClickListener {
            DialogUtil.showConfirmationDialog(
                context = this,
                title = "Cancel Subscription",
                message = "Are you sure you want to cancel subscription?",
                onConfirmed = {
                    val subscriptionId = dashboardViewModel.subscriptionData.value?.id
                    if (subscriptionId != null) {
                        dashboardViewModel.cancelUserSubscription(subscriptionId)
                    }
                }
            )
            lifecycleScope.launch {
                dashboardViewModel.subscriptionStatus.collectLatest { result ->
                    if (result?.isSuccess == true) {
                        Toast.makeText(this@DashboardActivity, "Subscription canceled successfully", Toast.LENGTH_SHORT).show()
                    } else if (result?.isFailure == true) {
                        Toast.makeText(this@DashboardActivity, "Failed to cancel subscription", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun pauseSubscription() {
        binding.btnPause.setOnClickListener {
            val bottomSheet = PauseSubscriptionBottomSheet()
            bottomSheet.show(supportFragmentManager, PauseSubscriptionBottomSheet::class.java.simpleName)
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