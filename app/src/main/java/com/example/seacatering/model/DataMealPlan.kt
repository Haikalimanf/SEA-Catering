package com.example.seacatering.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataMealPlan(
    val id: String = "",
    val name: String = "",
    val shortDescription: String = "",
    val longDescription: String = "",
    val price: Int = 0,
    val imageUri: String = "",
    val benefits: List<String> = emptyList(),
    val weeklyMenu: List<String> = emptyList(),
    val nutritionalInfo: List<String> = emptyList()
) : Parcelable


