package com.example.seacatering.ui.admin.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seacatering.model.DataAnalyticsResult
import com.example.seacatering.repository.AdminAnalyticsRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val adminDashboardRepository: AdminAnalyticsRepository
): ViewModel() {

    private val _dataAnalytics = MutableStateFlow<DataAnalyticsResult?>(null)
    val dataAnalytics: StateFlow<DataAnalyticsResult?> = _dataAnalytics

    fun fetchDashboardAnalytics(start: Timestamp, end: Timestamp) {
        viewModelScope.launch {
            val result = adminDashboardRepository.getDashboardAnalytics(start, end)
            Log.d("fetchDashboardAnalytics", "fetchDashboardAnalytics: $result")
            if (result.isSuccess) {
                _dataAnalytics.value = result.getOrNull()
            } else {
                _dataAnalytics.value = null
            }
        }
    }
}