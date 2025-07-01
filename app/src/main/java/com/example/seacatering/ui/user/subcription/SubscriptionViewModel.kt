package com.example.seacatering.ui.user.subcription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seacatering.model.DataSubscription
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

    private val _canSubscribe = MutableStateFlow<Boolean>(true)
    val canSubscribe: StateFlow<Boolean> = _canSubscribe

    fun addSubscription(dataSubscription: DataSubscription) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            val now = Timestamp.now()

            val previousCancelled = cateringRepository.getLatestCancelledSubscription(userId)

            var newSubscription = dataSubscription.copy(
                id = UUID.randomUUID().toString(),
                userId = userId
            )

            if (previousCancelled != null) {
                newSubscription = newSubscription.copy(
                    reactivated_at = now,
                )
            }


            val result = cateringRepository.saveSubscription(newSubscription)
            if (result.isSuccess) {
                _subscriptions.value += newSubscription
            }
        }
    }

    fun checkUserCanSubscribe() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId() ?: return@launch
            val result = cateringRepository.canUserSubscribe(userId)
            _canSubscribe.value = result
        }
    }

}


