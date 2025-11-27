package com.putradwicahyono.ime.ui.upcoming

import com.putradwicahyono.ime.domain.model.Anime

/**
 * Upcoming State
 * UI State untuk Upcoming Screen
 *
 * File: ui/upcoming/UpcomingState.kt
 */
data class UpcomingState(
    // Available years (current + 2 years ke depan)
    val availableYears: List<Int> = emptyList(),

    // Selected year tab
    val selectedYear: Int = 0,

    // All upcoming anime (semua tahun)
    val allUpcomingAnime: List<Anime> = emptyList(),

    // Filtered anime by selected year
    val displayedAnime: List<Anime> = emptyList(),

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
        return displayedAnime.isNotEmpty()
    }

    /**
     * Check if can load more
     */
    fun canLoadMore(): Boolean {
        return hasNextPage && !isLoadingMore && !isLoading
    }

    /**
     * Get anime count untuk selected year
     */
    fun getAnimeCount(): Int {
        return displayedAnime.size
    }

    /**
     * Get total anime count (all years)
     */
    fun getTotalAnimeCount(): Int {
        return allUpcomingAnime.size
    }
}