package com.example.seacatering.ui.user.dashboard.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.seacatering.R
import com.example.seacatering.databinding.FragmentPauseSubscriptionBottomSheetBinding
import com.example.seacatering.ui.user.dashboard.DashboardViewModel
import com.example.seacatering.utils.DateTimePickerUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PauseSubscriptionBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentPauseSubscriptionBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private var selectedStartDate: String? = null
    private var selectedEndDate: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPauseSubscriptionBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataSubcription()
        selectedDate()
        confirmPause()


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dashboardViewModel.subscriptionStatus.collectLatest { result ->
                    if (result?.isSuccess == true) {
                        Toast.makeText(requireContext(), "Subscription paused successfully", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else if (result?.isFailure == true) {
                        Toast.makeText(requireContext(), "Failed to pause subscription", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun confirmPause() {
        binding.btnConfirmPause.setOnClickListener {
            if (selectedStartDate == null || selectedEndDate == null) {
                binding.tvSelectedDate.error = "Please select a date"
                return@setOnClickListener
            }
            val subscriptionId = dashboardViewModel.subscriptionData.value?.id

            if (subscriptionId != null) {
                dashboardViewModel.pauseUserSubscription(
                    subscriptionId,
                    selectedStartDate!!,
                    selectedEndDate!!
                )
                Toast.makeText(requireContext(), "Subscription paused", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun selectedDate() {
        binding.tvSelectedDate.setOnClickListener {
            DateTimePickerUtil.showDateRangePicker(requireContext()) { startDate, endDate ->
                selectedStartDate = startDate
                selectedEndDate = endDate
                binding.tvSelectedDate.text = "$startDate to $endDate"
                binding.tvSelectedDate.setTextColor(resources.getColor(R.color.black, null))
            }
        }

        binding.btnCalendar.setOnClickListener {
            binding.tvSelectedDate.performClick()
        }
    }

    private fun fetchDataSubcription() {
        dashboardViewModel.fetchUserSubscription()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}