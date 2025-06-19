package com.example.seacatering.ui.user.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seacatering.model.DataAdvantages
import com.example.seacatering.model.DataMealPlan
import com.example.seacatering.repository.CateringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CateringRepository
) : ViewModel() {

    private val _advantages = MutableStateFlow<List<DataAdvantages>>(emptyList())
    val advantages: StateFlow<List<DataAdvantages>> = _advantages

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchMealPlans()
    }

    private fun fetchMealPlans() {
        viewModelScope.launch {
            val result = repository.getAllAdvantages()
            if (result.isSuccess) {
                _advantages.value = result.getOrNull() ?: emptyList()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Unknown error"
            }
        }
    }

}