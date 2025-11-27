package com.putradwicahyono.ime.ui.home

import com.putradwicahyono.ime.domain.model.Anime
import com.putradwicahyono.ime.util.Resource

/**
 * Home State
 * UI State untuk Home Screen
 *
 * File: ui/home/HomeState.kt
 */
data class HomeState(
    // Hero Section (Random Anime)
    val heroAnime: Resource<Anime> = Resource.Loading(),

    // Top 10 Anime Section
    val topAnime: Resource<List<Anime>> = Resource.Loading(),
    val isTopExpanded: Boolean = false,

    // Anime Musim Ini Section
    val currentSeasonAnime: Resource<List<Anime>> = Resource.Loading(),
    val currentSeason: String = "",
    val currentYear: Int = 0,

    // Anime Musim Lalu Section
    val previousSeasonAnime: Resource<List<Anime>> = Resource.Loading(),
    val previousSeason: String = "",
    val previousYear: Int = 0,

    // Anime Upcoming Section
    val upcomingAnime: Resource<List<Anime>> = Resource.Loading(),

    // Loading & Error states
    val isRefreshing: Boolean = false,
    val error: String? = null
) {
    /**
     * Get displayed top anime (6 atau 10 berdasarkan expand state)
     */
    fun getDisplayedTopAnime(): List<Anime> {
        return when (topAnime) {
            is Resource.Success -> {
                val data = topAnime.data ?: emptyList()
                if (isTopExpanded) data.take(10) else data.take(4)
            }
            else -> emptyList()
        }
    }

    /**
     * Check if all data loaded successfully
     */
    fun isAllDataLoaded(): Boolean {
        return heroAnime is Resource.Success &&
                topAnime is Resource.Success &&
                currentSeasonAnime is Resource.Success &&
                previousSeasonAnime is Resource.Success &&
                upcomingAnime is Resource.Success
    }

    /**
     * Check if any data is loading
     */
    fun isAnyDataLoading(): Boolean {
        return heroAnime is Resource.Loading ||
                topAnime is Resource.Loading ||
                currentSeasonAnime is Resource.Loading ||
                previousSeasonAnime is Resource.Loading ||
                upcomingAnime is Resource.Loading
    }

    /**
     * Check if has any error
     */
    fun hasError(): Boolean {
        return heroAnime is Resource.Error ||
                topAnime is Resource.Error ||
                currentSeasonAnime is Resource.Error ||
                previousSeasonAnime is Resource.Error ||
                upcomingAnime is Resource.Error
    }
}
