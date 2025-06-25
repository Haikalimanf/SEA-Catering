package com.example.seacatering.ui.user.subcription

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.seacatering.databinding.FragmentSubscriptionBinding
import com.example.seacatering.model.DataSubscription
import com.example.seacatering.model.enums.SubscriptionStatus
import com.example.seacatering.ui.user.checkout.CheckoutActivity
import com.example.seacatering.ui.user.menu.MenuViewModel
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class SubscriptionFragment : Fragment() {

    private var _binding: FragmentSubscriptionBinding? = null
    private val binding get() = _binding!!

    private val menuViewModel: MenuViewModel by viewModels()

    private val radioButtons by lazy {
        mapOf(
            binding.rbPlan1 to (0 to "Diet Plan"),
            binding.rbPlan2 to (1 to "Protein Plan"),
            binding.rbPlan3 to (2 to "Royal Plan")
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRadioButtons()
        setupSubscriptionFlow()
    }

    private fun setupSubscriptionFlow() {
        binding.btnSubcribe.setOnClickListener {
            if (!validateInputs()) return@setOnClickListener

            val username = binding.edtUsername.text.toString().trim()
            val phone = binding.edtPhoneNumber.text.toString().trim()
            val selectedPlanIndex = getSelectedPlanIndex()
            val selectedPlanText = getSelectedPlan()
            val mealPlan = getSelectedMeals()
            val deliveryDays = getSelectedDeliveryDays()
            val allergies = binding.edtAllergies.text.toString().trim()

            if (selectedPlanText == null) {
                Toast.makeText(requireContext(), "Please select a plan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, 1)
            val oneMonthLater = Timestamp(calendar.time)

            lifecycleScope.launch {
                val mealPlans = menuViewModel.mealPlans.firstOrNull { it.isNotEmpty() }

                if (mealPlans == null) {
                    Toast.makeText(requireContext(), "Meal plans not available", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val matchedPlan = mealPlans.find { it.name == selectedPlanText }

                if (matchedPlan == null) {
                    Toast.makeText(requireContext(), "Selected plan not found", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val planPrice = matchedPlan.price
                val mealCount = mealPlan.size
                val dayCount = deliveryDays.size
                val totalPrice = (planPrice * mealCount * dayCount * 4.3).toInt()

                val subscription = DataSubscription(
                    id = "",
                    userId = "",
                    username = username,
                    phone_number = phone,
                    plan_id = selectedPlanIndex,
                    plan_type_name = selectedPlanText,
                    meal_plan = mealPlan,
                    delivery_days = deliveryDays,
                    allergies = allergies,
                    total_price = totalPrice,
                    status = SubscriptionStatus.ACTIVE,
                    end_date = oneMonthLater,
                    pause_periode_start = null,
                    pause_periode_end = null,
                    last_paused_at = null,
                    last_cancelled_at = null,
                    reactivated_at = null
                )

                val intent = Intent(requireContext(), CheckoutActivity::class.java).apply {
                    putExtra("checkout_data", subscription)
                }
                startActivity(intent)
            }
        }
    }


    private fun setupRadioButtons() {
        radioButtons.keys.forEach { radioButton ->
            radioButton.setOnCheckedChangeListener { selected, isChecked ->
                if (isChecked) {
                    radioButtons.keys
                        .filterNot { it == selected }
                        .forEach { it.isChecked = false }
                }
            }
        }
    }

    private fun getSelectedPlan(): String? {
        return radioButtons.entries.find { it.key.isChecked }?.value?.second
    }

    private fun getSelectedPlanIndex(): Int {
        return radioButtons.entries.find { it.key.isChecked }?.value?.first ?: -1
    }

    private fun getSelectedMeals(): List<String> {
        val meals = mutableListOf<String>()
        if (binding.cbBreakfest.isChecked) meals.add("Breakfast")
        if (binding.cbLaunch.isChecked) meals.add("Lunch")
        if (binding.cbDinner.isChecked) meals.add("Dinner")
        return meals
    }

    private fun getSelectedDeliveryDays(): List<String> {
        val days = mutableListOf<String>()
        if (binding.cbMonday.isChecked) days.add("Monday")
        if (binding.cbTuesday.isChecked) days.add("Tuesday")
        if (binding.cbWednesday.isChecked) days.add("Wednesday")
        if (binding.cbThursday.isChecked) days.add("Thursday")
        if (binding.cbFriday.isChecked) days.add("Friday")
        if (binding.cbSaturday.isChecked) days.add("Saturday")
        if (binding.cbSunday.isChecked) days.add("Sunday")
        return days
    }

    private fun isMealTypeSelected(): Boolean {
        return binding.cbBreakfest.isChecked || binding.cbLaunch.isChecked || binding.cbDinner.isChecked
    }

    private fun isDeliveryDaySelected(): Boolean {
        return binding.cbMonday.isChecked || binding.cbTuesday.isChecked || binding.cbWednesday.isChecked ||
                binding.cbThursday.isChecked || binding.cbFriday.isChecked || binding.cbSaturday.isChecked ||
                binding.cbSunday.isChecked
    }

    private fun validateInputs(): Boolean {
        val username = binding.edtUsername.text.toString().trim()
        val phone = binding.edtPhoneNumber.text.toString().trim()
        val selectedPlan = getSelectedPlan()
        val isMealChecked = isMealTypeSelected()
        val isDayChecked = isDeliveryDaySelected()

        return when {
            username.isEmpty() -> {
                binding.edtUsername.error = "Please enter your name"
                false
            }
            phone.isEmpty() -> {
                binding.edtPhoneNumber.error = "Please enter your phone number"
                false
            }
            selectedPlan == null -> {
                Toast.makeText(requireContext(), "Please select a subscription plan", Toast.LENGTH_SHORT).show()
                false
            }
            !isMealChecked -> {
                Toast.makeText(requireContext(), "Please select at least one meal type", Toast.LENGTH_SHORT).show()
                false
            }
            !isDayChecked -> {
                Toast.makeText(requireContext(), "Please select at least one delivery day", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
