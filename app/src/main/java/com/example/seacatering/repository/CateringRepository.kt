package com.example.seacatering.repository

import com.example.seacatering.model.DataMealPlan
import com.example.seacatering.model.DataSubscription
import com.example.seacatering.model.DataTestimonial
import com.example.seacatering.model.DataUser
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

    suspend fun updateUser(user: DataUser): Result<Void?> {
        return try {
            val docRef = firestore.collection("users").document(user.id)
            docRef.set(user).await() // overwrite dokumen dengan user.id
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
}
