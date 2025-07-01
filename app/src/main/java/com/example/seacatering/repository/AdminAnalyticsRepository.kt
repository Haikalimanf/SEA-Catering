package com.example.seacatering.repository

import android.util.Log
import com.example.seacatering.model.DataAnalyticsResult
import com.example.seacatering.model.DataSubscription
import com.example.seacatering.model.enums.SubscriptionStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminAnalyticsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private suspend fun getNewSubscriptions(start: Timestamp, end: Timestamp): Result<Int> {
        return try {
            val snapshot = firestore.collection("subscriptions")
                .whereGreaterThanOrEqualTo("created_at", start)
                .whereLessThanOrEqualTo("created_at", end)
                .get()
                .await()

            val count = snapshot.size()
            Result.success(count)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getRecurringRevenue(
        periodStart: Timestamp,
        periodEnd: Timestamp
    ): Result<Int> {
        return try {
            val snapshot = firestore.collection("subscriptions")
                .whereLessThanOrEqualTo("created_at", periodEnd)
                .whereGreaterThanOrEqualTo("created_at", periodStart)
                .get()
                .await()

            val subscriptions = snapshot.toObjects(DataSubscription::class.java)
            val totalRevenue = subscriptions.sumOf { it.total_price }

            Result.success(totalRevenue)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReactivations(
        periodStart: Timestamp,
        periodEnd: Timestamp
    ): Result<Int> {
        return try {
            val snapshot = firestore.collection("subscriptions")
                .whereGreaterThan("reactivated_at", Timestamp(0, 0))
                .whereGreaterThanOrEqualTo("reactivated_at", periodStart)
                .whereLessThanOrEqualTo("reactivated_at", periodEnd)
                .get()
                .await()

            Result.success(snapshot.size())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private suspend fun getSubscriptionActive(
    ) : Result<Int>{
        return try {
            val snapshot = firestore.collection("subscriptions")
                .whereEqualTo("status", SubscriptionStatus.ACTIVE.name)
                .get()
                .await()

            val count = snapshot.size()
            Result.success(count)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getDashboardAnalytics(start: Timestamp, end: Timestamp): Result<DataAnalyticsResult> {
        return try {

            val newSubs = getNewSubscriptions(start, end).getOrDefault(0)
            val mrr = getRecurringRevenue(start, end).getOrDefault(0)
            val reactivations = getReactivations(start, end).getOrDefault(0)
            val growth = getSubscriptionActive().getOrDefault(0)

            Result.success(
                DataAnalyticsResult(
                    newSubscriptions = newSubs,
                    recurringRevenue = mrr,
                    reactivations = reactivations,
                    subscriptionActive = growth
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}