package com.example.seacatering.model.state

import com.example.seacatering.model.auth.AuthOutcome

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val outcome: AuthOutcome) : AuthState()
    data class Error(val message: String) : AuthState()
}