package com.example.seacatering.ui.user.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seacatering.model.DataSubscription
import com.example.seacatering.model.enums.SubscriptionStatus
import com.example.seacatering.repository.AuthRepository
import com.example.seacatering.repository.CateringRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val cateringRepository: CateringRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _subcriptionData = MutableStateFlow<DataSubscription?>(null)
    val subscriptionData: StateFlow<DataSubscription?> = _subcriptionData

    private val _subscriptionStatus = MutableStateFlow<Result<Void?>?>(null)
    val subscriptionStatus: StateFlow<Result<Void?>?> = _subscriptionStatus

    init {
        fetchUserSubscription()
    }

    fun fetchUserSubscription() {
        val userId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            val result = cateringRepository.getUserSubscriptions(userId)

            if (result.isSuccess) {
                val subscriptions = result.getOrNull().orEmpty()
                val latestActiveOrPaused = subscriptions
                    .filter { it.status == SubscriptionStatus.ACTIVE || it.status == SubscriptionStatus.PAUSED }
                    .maxByOrNull { it.end_date.toDate().time }

                _subcriptionData.value = latestActiveOrPaused
            }
        }
    }


    fun cancelUserSubscription(subscriptionId: String) {
        viewModelScope.launch {
            val result = cateringRepository.cancelSubscription(subscriptionId)
            _subscriptionStatus.value = result
            if (result.isSuccess) fetchUserSubscription()
        }
    }


    fun pauseUserSubscription(subscriptionId: String, start: String, end: String) {
        viewModelScope.launch {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startTs = Timestamp(sdf.parse(start)!!)
            val endTs   = Timestamp(sdf.parse(end)!!)
            val result  = cateringRepository.pauseSubscription(subscriptionId, startTs, endTs)
            if (result.isSuccess) {
                _subscriptionStatus.value = result
                fetchUserSubscription()
            }
        }
    }

}