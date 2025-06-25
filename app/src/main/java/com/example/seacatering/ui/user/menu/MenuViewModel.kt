package com.example.seacatering.ui.user.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seacatering.model.DataMealPlan
import com.example.seacatering.repository.CateringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: CateringRepository
) : ViewModel() {

    private val _mealPlans = MutableStateFlow<List<DataMealPlan>>(emptyList())
    val mealPlans: StateFlow<List<DataMealPlan>> = _mealPlans

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchMealPlans()
    }

    private fun fetchMealPlans() {
        viewModelScope.launch {
            val result = repository.getAllMealPlans()
            if (result.isSuccess) {
                _mealPlans.value = result.getOrNull() ?: emptyList()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Unknown error"
            }
        }
    }

}