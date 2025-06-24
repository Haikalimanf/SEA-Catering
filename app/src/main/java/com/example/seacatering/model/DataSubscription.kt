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
    val meal_plan: List<String> = emptyList(),
    val delivery_days: List<String> = emptyList(),
    val allergies: String = "",
    val total_price: Int = 0,
    val status: SubscriptionStatus = SubscriptionStatus.ACTIVE,
    val created_at: Timestamp = Timestamp.now(),
    val end_date: Timestamp = Timestamp.now(),
    val pause_periode_start: Timestamp? = null,
    val pause_periode_end: Timestamp? = null,
    val last_paused_at: Timestamp? = null,
    val last_cancelled_at: Timestamp? = null,
    val reactivated_at: Timestamp? = null
) : Parcelable
