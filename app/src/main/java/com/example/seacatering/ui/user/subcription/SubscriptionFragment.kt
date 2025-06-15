package com.example.seacatering.ui.user.subcription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.seacatering.databinding.FragmentSubscriptionBinding

class SubscriptionFragment : Fragment() {

    private var _binding: FragmentSubscriptionBinding? = null
    private val binding get() = _binding!!

    private val radioButtons by lazy {
        listOf(binding.rbPlan1, binding.rbPlan2, binding.rbPlan3)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val subscriptionViewModel =
            ViewModelProvider(this).get(SubscriptionViewModel::class.java)

        _binding = FragmentSubscriptionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRadioButtons()

        binding.btnSubcribe.setOnClickListener {
            if (validateInputs()) {
                val selectedPlan = getSelectedPlan()
                val allergies = binding.edtAllergies.text.toString().trim()

                Toast.makeText(requireContext(), "All input valid. Plan: $selectedPlan", Toast.LENGTH_SHORT).show()
            }
        }


        return root
    }

    private fun setupRadioButtons() {
        radioButtons.forEach { radioButton ->
            radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    radioButtons.filterNot { it == buttonView }
                        .forEach { it.isChecked = false }
                }
            }
        }
    }

    private fun getSelectedPlan(): String? {
        return radioButtons.find { it.isChecked }?.text?.toString()
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
        val username = binding.edtEmail.text.toString().trim()
        val phone = binding.edtPhoneNumber.text.toString().trim()
        val selectedPlan = getSelectedPlan()
        val isMealChecked = isMealTypeSelected()
        val isDayChecked = isDeliveryDaySelected()

        return when {
            username.isEmpty() -> {
                binding.edtEmail.error = "Please enter your name"
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