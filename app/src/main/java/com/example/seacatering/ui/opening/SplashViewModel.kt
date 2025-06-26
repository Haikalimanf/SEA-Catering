package com.example.seacatering.ui.opening

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seacatering.model.state.RoleResultState
import com.example.seacatering.repository.AuthRepository
import com.example.seacatering.repository.CateringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val cateringRepository: CateringRepository,
): ViewModel()
{
    private val _roleState = MutableStateFlow<RoleResultState>(RoleResultState.Loading)
    val roleState: StateFlow<RoleResultState> = _roleState

    fun checkUserRole() {
        val userId = authRepository.getCurrentUserId()
        if (userId == null) {
            _roleState.value = RoleResultState.NotLoggedIn
            return
        }

        Log.d("checkUserRole", "checkUserRole: $userId")

        viewModelScope.launch {
            val result = cateringRepository.getUserById(userId)
            if (result.isSuccess) {
                val role = result.getOrNull()?.role
                Log.d("checkUserRole", "checkUserRole: $role")
                if (role != null) {
                    _roleState.value = RoleResultState.Success(role)
                } else {
                    _roleState.value = RoleResultState.Error("User role not found.")
                }
            } else {
                _roleState.value = RoleResultState.Error("Failed to fetch user role.")
            }
        }
    }
}

