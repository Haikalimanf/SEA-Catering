package com.example.seacatering.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seacatering.repository.AuthRepository
import com.example.seacatering.utils.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> get() = _registerState

    fun register(email : String , password : String) {
        _registerState.value = AuthState.Loading

        viewModelScope.launch {
            val result = authRepository.register(email, password)
            if (result.isSuccess) {
                _registerState.value = AuthState.Success
            } else {
                _registerState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

}