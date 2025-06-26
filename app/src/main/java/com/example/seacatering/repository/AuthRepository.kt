package com.example.seacatering.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.seacatering.R
import com.example.seacatering.model.DataUser
import com.example.seacatering.model.auth.AuthOutcome
import com.example.seacatering.model.enums.UserRole
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val credentialManager: CredentialManager
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

    suspend fun loginWithGoogle(context: Context): Result<AuthOutcome> {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken

                val firebaseUser = firebaseAuth.signInWithCredential(
                    GoogleAuthProvider.getCredential(idToken, null)
                ).await()

                val user = firebaseUser.user ?: throw Exception("User kosong")
                val uid = user.uid
                val email = user.email.orEmpty()
                val name = user.displayName.orEmpty()

                val userDoc = firestore.collection("users").document(uid)
                val snapshot = userDoc.get().await()

                if (!snapshot.exists()) {
                    val newUser = DataUser(
                        id = uid,
                        email = email,
                        name = name,
                        role = UserRole.USER
                    )
                    userDoc.set(newUser).await()
                }

                Result.success(AuthOutcome(uid, email))
            } else {
                Result.failure(Exception("Credential is not Google ID Token"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}