package com.putradwicahyono.ime.di

import android.content.Context
import com.putradwicahyono.ime.data.preferences.AppPreferences
import com.putradwicahyono.ime.data.remote.JikanApiService
import com.putradwicahyono.ime.data.repository.AnimeRepository

/**
 * App Module - Manual Dependency Injection
 * Singleton pattern untuk provide dependencies
 *
 * File: di/AppModule.kt
 */
object AppModule {

    // Singleton AppPreferences
    @Volatile
    private var appPreferences: AppPreferences? = null

    /**
     * Provide AppPreferences (DataStore)
     * Thread-safe singleton dengan double-checked locking
     */
    fun provideAppPreferences(context: Context): AppPreferences {
        return appPreferences ?: synchronized(this) {
            appPreferences ?: AppPreferences(context.applicationContext).also {
                appPreferences = it
            }
        }
    }

    // Singleton AnimeRepository
    @Volatile
    private var animeRepository: AnimeRepository? = null

    /**
     * Provide AnimeRepository
     * Depends on: JikanApiService
     */
    fun provideAnimeRepository(apiService: JikanApiService): AnimeRepository {
        return animeRepository ?: synchronized(this) {
            animeRepository ?: AnimeRepository(apiService).also {
                animeRepository = it
            }
        }
    }

    /**
     * Reset semua dependencies (untuk testing atau logout)
     */
    fun reset() {
        appPreferences = null
        animeRepository = null
    }
}