package com.example.seacatering.repository

import com.example.seacatering.model.DataAdvantages
import com.example.seacatering.model.DataCheckout
import com.example.seacatering.model.DataMealPlan
import com.example.seacatering.model.DataSubscription
import com.example.seacatering.model.DataTestimonial
import com.example.seacatering.model.DataUser
import com.example.seacatering.model.enums.SubscriptionStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CateringRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun saveUser(dataUser: DataUser): Result<Void?> {
        return try {
            val docRef = firestore.collection("users").document(dataUser.id)
            docRef.set(dataUser).await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(userId: String): Result<DataUser?> {
        return try {
            val snapshot = firestore.collection("users").document(userId).get().await()
            val dataUser = snapshot.toObject(DataUser::class.java)
            Result.success(dataUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllMealPlans(): Result<List<DataMealPlan>> {
        return try {
            val snapshot = firestore.collection("meal_plans").get().await()
            val plans = snapshot.toObjects(DataMealPlan::class.java)
            Result.success(plans)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllAdvantages(): Result<List<DataAdvantages>> {
        return try {
            val snapshot = firestore.collection("advantages").get().await()
            val plans = snapshot.toObjects(DataAdvantages::class.java)
            Result.success(plans)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(user: DataUser): Result<Void?> {
        return try {
            val docRef = firestore.collection("users").document(user.id)
            docRef.set(user).await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveSubscription(dataSubscription: DataSubscription): Result<Void?> {
        return try {
            val docRef = firestore.collection("subscriptions").document(dataSubscription.id)
            docRef.set(dataSubscription).await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addCheckout(dataCheckout: DataCheckout): Result<Void?> {
        return try {
            val docRef = firestore.collection("checkout").document(dataCheckout.id)
            docRef.set(dataCheckout).await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addTestimonial(dataTestimonial: DataTestimonial): Result<Void?> {
        return try {
            val docRef = firestore.collection("testimonials").document(dataTestimonial.id)
            docRef.set(dataTestimonial).await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllTestimonials(): Result<List<DataTestimonial>> {
        return try {
            val snapshot = firestore.collection("testimonials").get().await()
            val testimonials = snapshot.toObjects(DataTestimonial::class.java)
            Result.success(testimonials)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserSubscriptions(userId: String): Result<List<DataSubscription>> {
        return try {
            val snapshot = firestore.collection("subscriptions")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val subscriptions = snapshot.toObjects(DataSubscription::class.java)
            Result.success(subscriptions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelSubscription(subscriptionId: String): Result<Void?> {
        return try {
            val subscriptionRef = firestore.collection("subscriptions").document(subscriptionId)
            subscriptionRef.update("status", SubscriptionStatus.CANCELED.name).await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun pauseSubscription(
        subscriptionId: String,
        pauseStart: String,
        pauseEnd: String
    ): Result<Void?> {
        return try {
            val subscriptionRef = firestore.collection("subscriptions").document(subscriptionId)
            subscriptionRef.update(
                mapOf(
                    "status" to SubscriptionStatus.PAUSED.name,
                    "pause_periode_start" to pauseStart,
                    "pause_periode_end" to pauseEnd
                )
            ).await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserCheckout(userId: String): Result<List<DataCheckout>> {
        return try {
            val snapshot = firestore.collection("checkout")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val checkout = snapshot.toObjects(DataCheckout::class.java)
            Result.success(checkout)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
