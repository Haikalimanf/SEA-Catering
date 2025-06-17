package com.example.seacatering.model.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthOutcome(
    val userId: String,
    val email: String?
) : Parcelable


