package com.example.seacatering.ui.user.subcription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seacatering.model.DataCheckout
import com.example.seacatering.model.DataSubscription
import com.example.seacatering.model.DataTestimonial
import com.example.seacatering.repository.AuthRepository
import com.example.seacatering.repository.CateringRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val cateringRepository: CateringRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _subscriptions = MutableStateFlow<List<DataSubscription>>(emptyList())
    val subscriptions: StateFlow<List<DataSubscription>> = _subscriptions

    suspend fun hasExistingSubscription(): Boolean {
        val userId = authRepository.getCurrentUserId() ?: return false
        val result = cateringRepository.getUserSubscriptions(userId)
        return result.isSuccess && result.getOrNull()?.isNotEmpty() == true
    }

    fun addSubscription(dataSubscription: DataSubscription) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId()
            if (userId != null) {
                val newSubscription = dataSubscription.copy(
                    id = UUID.randomUUID().toString(),
                    userId = userId
                )

                val result = cateringRepository.saveSubscription(newSubscription)
                if (result.isSuccess) {
                    _subscriptions.value = _subscriptions.value + newSubscription
                }
            }
        }
    }
}
