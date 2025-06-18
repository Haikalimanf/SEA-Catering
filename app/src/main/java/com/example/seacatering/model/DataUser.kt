package com.example.seacatering.model

import android.os.Parcelable
import com.example.seacatering.model.enums.UserRole
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUser(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.USER,
    val address: String = "",
    val imageUri: String = ""
): Parcelable