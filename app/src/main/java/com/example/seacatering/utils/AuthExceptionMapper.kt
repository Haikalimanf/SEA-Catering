package com.example.seacatering.utils

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

object AuthExceptionMapper {
    fun mapAuthException(e: Exception): String {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> "Password salah."
            is FirebaseAuthInvalidUserException -> "Akun tidak ditemukan."
            is FirebaseAuthUserCollisionException -> "Email sudah digunakan."
            else -> "Terjadi kesalahan: ${e.localizedMessage}"
        }
    }
}