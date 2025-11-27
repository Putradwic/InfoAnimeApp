package com.putradwicahyono.ime.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension untuk create DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class AppPreferences(private val context: Context) {

    companion object {
        // Keys untuk preferences
        private val THEME_KEY = booleanPreferencesKey("is_dark_mode")
        private val LANGUAGE_KEY = stringPreferencesKey("language")

        // Default values
        const val DEFAULT_LANGUAGE = "en" // "en" atau "id"
        const val DEFAULT_DARK_MODE = false
    }

    // Flow untuk observe dark mode
    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: DEFAULT_DARK_MODE
        }

    // Flow untuk observe language
    val language: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[LANGUAGE_KEY] ?: DEFAULT_LANGUAGE
        }

    // Save dark mode preference
    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }

    // Save language preference
    suspend fun setLanguage(languageCode: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languageCode
        }
    }

    // Toggle dark mode
    suspend fun toggleDarkMode() {
        context.dataStore.edit { preferences ->
            val currentMode = preferences[THEME_KEY] ?: DEFAULT_DARK_MODE
            preferences[THEME_KEY] = !currentMode
        }
    }
}
