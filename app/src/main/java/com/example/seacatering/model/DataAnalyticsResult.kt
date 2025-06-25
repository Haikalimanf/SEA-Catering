package com.example.seacatering.model

data class DataAnalyticsResult(
    val newSubscriptions: Int = 0,
    val recurringRevenue: Int? = 0,
    val reactivations: Int = 0,
    val subscriptionActive: Int = 0
)
