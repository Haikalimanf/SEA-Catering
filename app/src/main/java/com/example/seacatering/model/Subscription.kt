package com.example.seacatering.model

import android.os.Parcelable
import com.example.seacatering.model.enums.SubscriptionStatus
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subscription(
    val id: String,
    val userId: String,
    val phone_number: String,
    val plan_id: Int,
    val meal_plan: String,
    val delivery_days: List<String>,
    val allergies: List<String>,
    val status: SubscriptionStatus,
    val end_date: Timestamp,
    val pause_periode_start: String,
    val pause_periode_end: String
) : Parcelable
