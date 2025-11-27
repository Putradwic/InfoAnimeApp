package com.putradwicahyono.ime.ui.seasonal

import com.putradwicahyono.ime.domain.model.Anime

/**
 * Seasonal State
 * UI State untuk Seasonal Screen
 *
 * File: ui/seasonal/SeasonalState.kt
 */
data class SeasonalState(
    // Selected filters
    val selectedYear: Int,
    val selectedSeason: String,

    // Available years (untuk filter dropdown)
    val availableYears: List<Int> = emptyList(),

    // Seasonal anime list
    val seasonalAnime: List<Anime> = emptyList(),

    // Pagination
    val currentPage: Int = 1,
    val hasNextPage: Boolean = true,

    // Loading states
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,

    // Error
    val error: String? = null,

    // Initial load flag
    val isInitialLoad: Boolean = true
) {
    /**
     * Check if has data
     */
    fun hasData(): Boolean {
        return seasonalAnime.isNotEmpty()
    }

    /**
     * Check if can load more
     */
    fun canLoadMore(): Boolean {
        return hasNextPage && !isLoadingMore && !isLoading
    }

    /**
     * Get anime count
     */
    fun getAnimeCount(): Int {
        return seasonalAnime.size
    }

    /**
     * Get display title
     */
    fun getDisplayTitle(): String {
        val seasonCapitalized = selectedSeason.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
        return "$seasonCapitalized $selectedYear"
    }
}