package com.example.seacatering.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seacatering.model.DataUser
import com.example.seacatering.model.enums.UserRole
import com.example.seacatering.repository.AuthRepository
import com.example.seacatering.repository.CateringRepository
import com.example.seacatering.utils.AuthExceptionMapper
import com.example.seacatering.model.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val cateringRepository: CateringRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> get() = _registerState

    fun registerAndSaveUser(email: String, password: String, username: String) {
        _registerState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.register(email, password)
            if (result.isSuccess) {
                val outcome = result.getOrNull()!!
                val dataUser = DataUser(
                    id = outcome.userId,
                    name = username,
                    email = outcome.email ?: email,
                    role = UserRole.USER
                )
                cateringRepository.saveUser(dataUser)
                _registerState.value = AuthState.Success(outcome)
            } else {
                val e = result.exceptionOrNull() as? Exception
                val message = AuthExceptionMapper.mapAuthException(e ?: Exception("Unknown"))
                _registerState.value = AuthState.Error(message)
            }
        }
    }

}
