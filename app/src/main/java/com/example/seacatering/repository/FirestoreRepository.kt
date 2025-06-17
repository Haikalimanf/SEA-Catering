package com.example.seacatering.repository

import com.example.seacatering.model.MealPlan
import com.example.seacatering.model.Subscription
import com.example.seacatering.model.Testimonial
import com.example.seacatering.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun saveUser(user: User): Result<Void?> {
        return try {
            val docRef = firestore.collection("users").document(user.id)
            docRef.set(user).await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(userId: String): Result<User?> {
        return try {
            val snapshot = firestore.collection("users").document(userId).get().await()
            val user = snapshot.toObject(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllMealPlans(): Result<List<MealPlan>> {
        return try {
            val snapshot = firestore.collection("meal_plans").get().await()
            val plans = snapshot.toObjects(MealPlan::class.java)
            Result.success(plans)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveSubscription(subscription: Subscription): Result<Void?> {
        return try {
            val docRef = firestore.collection("subscriptions").document(subscription.id)
            docRef.set(subscription).await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addTestimonial(testimonial: Testimonial): Result<Void?> {
        return try {
            val docRef = firestore.collection("testimonials").document(testimonial.id)
            docRef.set(testimonial).await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllTestimonials(): Result<List<Testimonial>> {
        return try {
            val snapshot = firestore.collection("testimonials").get().await()
            val testimonials = snapshot.toObjects(Testimonial::class.java)
            Result.success(testimonials)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserSubscriptions(userId: String): Result<List<Subscription>> {
        return try {
            val snapshot = firestore.collection("subscriptions")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val subscriptions = snapshot.toObjects(Subscription::class.java)
            Result.success(subscriptions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
