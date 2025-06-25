package com.example.seacatering.ui.user.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seacatering.model.DataCheckout
import com.example.seacatering.repository.AuthRepository
import com.example.seacatering.repository.CateringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cateringRepository: CateringRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _checkoutData = MutableStateFlow<DataCheckout?>(null)
    val checkoutData: StateFlow<DataCheckout?> = _checkoutData

    init {
        fetchUserCheckout()
    }

    fun addCheckOut(dataCheckout: DataCheckout) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId()
            if (userId != null) {
                val newCheckout = dataCheckout.copy(
                    id = UUID.randomUUID().toString(),
                    userId = userId
                )

                val result = cateringRepository.addCheckout(newCheckout)
                if (result.isSuccess) {

                }
            }
        }
    }

    fun fetchUserCheckout() {
        val userId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            val result = cateringRepository.getUserCheckout(userId)
            if (result.isSuccess) {
                val latestCheckout = result.getOrNull()?.lastOrNull()
                _checkoutData.value = latestCheckout
            }

        }
    }

}