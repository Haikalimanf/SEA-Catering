package com.example.seacatering.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MealPlan(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUri: String
) : Parcelable
