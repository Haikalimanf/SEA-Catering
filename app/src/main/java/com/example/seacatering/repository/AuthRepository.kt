package com.example.seacatering.repository

import com.example.seacatering.model.DataUser
import com.example.seacatering.model.auth.AuthOutcome
import com.example.seacatering.model.enums.UserRole
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun login(email: String, password: String): Result<AuthOutcome> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw IllegalStateException("User kosong")
            val outcome = AuthOutcome(user.uid, user.email)
            Result.success(outcome)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<AuthOutcome> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw IllegalStateException("User kosong")
            val outcome = AuthOutcome(user.uid, user.email)
            Result.success(outcome)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    suspend fun getCurrentUserData(): DataUser? {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null
        val snapshot = FirebaseFirestore.getInstance().collection("users").document(uid).get().await()
        return snapshot.toObject(DataUser::class.java)
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun getCurrentUserRole(): UserRole? {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: return null
            val snapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            val roleString = snapshot.getString("role") ?: return null
            UserRole.valueOf(roleString.uppercase())
        } catch (e: Exception) {
            null
        }
    }


}