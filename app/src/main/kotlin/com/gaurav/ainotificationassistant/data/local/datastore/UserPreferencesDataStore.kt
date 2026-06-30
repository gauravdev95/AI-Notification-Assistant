package com.gaurav.ainotificationassistant.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferencesDataStore(private val context: Context) {

    companion object {
        private val GEMINI_API_KEY = stringPreferencesKey("gemini_api_key")
        private val DARK_MODE = stringPreferencesKey("dark_mode")
        private val RETENTION_DAYS = longPreferencesKey("retention_days")
        private val NOTIFICATION_FILTER_ENABLED = booleanPreferencesKey("notification_filter_enabled")
        private val INCLUDE_LOW_PRIORITY = booleanPreferencesKey("include_low_priority")
    }

    val geminiApiKey: Flow<String> = context.userPreferencesDataStore.data.map { preferences ->
        preferences[GEMINI_API_KEY] ?: ""
    }

    val darkMode: Flow<String> = context.userPreferencesDataStore.data.map { preferences ->
        preferences[DARK_MODE] ?: "system"
    }

    val retentionDays: Flow<Long> = context.userPreferencesDataStore.data.map { preferences ->
        preferences[RETENTION_DAYS] ?: 30L
    }

    val notificationFilterEnabled: Flow<Boolean> = context.userPreferencesDataStore.data.map { preferences ->
        preferences[NOTIFICATION_FILTER_ENABLED] ?: true
    }

    val includeLowPriority: Flow<Boolean> = context.userPreferencesDataStore.data.map { preferences ->
        preferences[INCLUDE_LOW_PRIORITY] ?: false
    }

    suspend fun setGeminiApiKey(apiKey: String) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[GEMINI_API_KEY] = apiKey
        }
    }

    suspend fun setDarkMode(mode: String) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[DARK_MODE] = mode
        }
    }

    suspend fun setRetentionDays(days: Long) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[RETENTION_DAYS] = days
        }
    }

    suspend fun setNotificationFilterEnabled(enabled: Boolean) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[NOTIFICATION_FILTER_ENABLED] = enabled
        }
    }

    suspend fun setIncludeLowPriority(include: Boolean) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[INCLUDE_LOW_PRIORITY] = include
        }
    }

    suspend fun clearAll() {
        context.userPreferencesDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
