package com.example.seacatering.ui.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seacatering.model.DataUser
import com.example.seacatering.repository.AuthRepository
import com.example.seacatering.repository.CateringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val cateringRepository: CateringRepository,
) : ViewModel() {

    private val _logoutState = MutableStateFlow(false)
    val logoutState: StateFlow<Boolean> = _logoutState

    private val _userState = MutableStateFlow<DataUser?>(null)
    val userState: StateFlow<DataUser?> = _userState

    fun logout() {
        authRepository.logout()
        _logoutState.value = true
    }

    fun fetchCurrentUser() {
        val userId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            val result = cateringRepository.getUserById(userId)
            if (result.isSuccess) {
                _userState.value = result.getOrNull()
            }
        }
    }

    fun updateUserProfile(name: String, address: String) {
        val currentUser = _userState.value ?: return

        val updatedUser = currentUser.copy(
            name = name,
            address = address
        )

        viewModelScope.launch {
            val result = cateringRepository.updateUser(updatedUser)
            if (result.isSuccess) {
                _userState.value = updatedUser
            }
        }
    }

}