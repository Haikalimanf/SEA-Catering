package com.example.seacatering.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seacatering.repository.AuthRepository
import com.example.seacatering.utils.AuthExceptionMapper
import com.example.seacatering.model.state.AuthState
import com.example.seacatering.model.state.RoleResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> get() = _loginState

    private val _roleState = MutableStateFlow<RoleResultState>(RoleResultState.Loading)
    val roleState: StateFlow<RoleResultState> = _roleState

    fun login(email: String, password: String) {
        _loginState.value = AuthState.Loading

        viewModelScope.launch {
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                val outcome = result.getOrNull()!!
                _loginState.value = AuthState.Success(outcome)
            } else {
                val error = result.exceptionOrNull() as? Exception
                val message = AuthExceptionMapper.mapAuthException(error ?: Exception("Unknown error"))
                _loginState.value = AuthState.Error(message)
            }
        }
    }

    fun checkUserRole() {
        viewModelScope.launch {
            val role = authRepository.getCurrentUserRole()
            if (role != null) {
                _roleState.value = RoleResultState.Success(role)
            } else {
                _roleState.value = RoleResultState.NotLoggedIn
            }
        }
    }
}
