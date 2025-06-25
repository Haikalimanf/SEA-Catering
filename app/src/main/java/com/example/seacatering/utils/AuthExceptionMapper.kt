package com.example.seacatering.utils

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

object AuthExceptionMapper {
    fun mapAuthException(e: Exception): String {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> "Incorrect password."
            is FirebaseAuthInvalidUserException -> "Account not found."
            is FirebaseAuthUserCollisionException -> "Email already in use."
            else -> "An error occurred: ${e.localizedMessage}"
        }
    }
}