package com.example.seacatering.model

import android.os.Parcelable
import com.example.seacatering.model.enums.SubscriptionStatus
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataSubscription(
    val id: String = "",
    val userId: String = "",
    val username: String = "",
    val phone_number: String = "",
    val plan_id: Int = 0,
    val plan_type_name: String = "",
    val meal_plan: String = "",
    val delivery_days: List<String> = emptyList(),
    val allergies: String = "",
    val status: SubscriptionStatus = SubscriptionStatus.ACTIVE,
    val end_date: Timestamp = Timestamp.now(),
    val pause_periode_start: String = "",
    val pause_periode_end: String = ""
) : Parcelable
