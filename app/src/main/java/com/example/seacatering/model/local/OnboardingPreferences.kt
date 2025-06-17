package com.example.seacatering.model.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object OnboardingPreferences {
    private const val ONBOARDING_KEY = "onboarding_completed"
    private val Context.dataStore by preferencesDataStore("onboarding_prefs")

    suspend fun setOnboardingCompleted(context: Context) {
        context.dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(ONBOARDING_KEY)] = true
        }
    }

    suspend fun isOnboardingCompleted(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[booleanPreferencesKey(ONBOARDING_KEY)] ?: false
    }
}
