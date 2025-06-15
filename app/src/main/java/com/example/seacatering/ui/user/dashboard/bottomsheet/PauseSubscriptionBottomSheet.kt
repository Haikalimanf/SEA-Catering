package com.example.seacatering.ui.user.dashboard.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seacatering.R
import com.example.seacatering.databinding.FragmentPauseSubscriptionBottomSheetBinding
import com.example.seacatering.utils.DateTimePickerUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class PauseSubscriptionBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentPauseSubscriptionBottomSheetBinding? = null
    private val binding get() = _binding!!

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

        binding.tvSelectedDate.setOnClickListener {
            DateTimePickerUtil.showDateRangePicker(requireContext()) { startDate, endDate ->
                binding.tvSelectedDate.text = "$startDate to $endDate"
                binding.tvSelectedDate.setTextColor(resources.getColor(R.color.black, null))
            }
        }

        binding.btnCalendar.setOnClickListener {
            binding.tvSelectedDate.performClick()
        }

        binding.btnConfirmPause.setOnClickListener {
            val date = binding.tvSelectedDate.text.toString()
            if (date == getString(R.string.select_date)) {
                binding.tvSelectedDate.error = "Please select a date"
                return@setOnClickListener
            }

            dismiss()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}