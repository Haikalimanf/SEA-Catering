package com.example.seacatering.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataCheckout(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val phone_number: String = "",
    val plan_id: Int = 0,
    val price: Int = 0,
    val plan_type_name: String = "",
    val meal_plan: List<String> = emptyList(),
    val delivery_days: List<String> = emptyList(),
    val allergies: String= ""
) : Parcelable
