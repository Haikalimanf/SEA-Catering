package com.example.seacatering.model.state

import com.example.seacatering.model.enums.UserRole

sealed class RoleResultState {
    data class Success(val role: UserRole) : RoleResultState()
    data class Error(val message: String) : RoleResultState()
    object NotLoggedIn : RoleResultState()
    object Loading : RoleResultState()
}
