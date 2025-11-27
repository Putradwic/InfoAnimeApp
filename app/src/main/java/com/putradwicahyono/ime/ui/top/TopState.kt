package com.putradwicahyono.ime.ui.top

import com.putradwicahyono.ime.domain.model.Anime
import com.putradwicahyono.ime.util.Resource

/**
 * Top State
 * UI State untuk Top Screen
 *
 * File: ui/top/TopState.kt
 */
data class TopState(
    // Top anime list
    val topAnime: List<Anime> = emptyList(),


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
        return topAnime.isNotEmpty()
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
        return topAnime.size
    }
}