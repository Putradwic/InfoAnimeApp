package com.putradwicahyono.ime.ui.search

import com.putradwicahyono.ime.domain.model.Anime
import com.putradwicahyono.ime.util.Resource

/**
 * Search State
 * UI State untuk Search Screen
 *
 * File: ui/search/SearchState.kt
 */
data class SearchState(
    // Search query input
    val query: String = "",

    // Search results
    val searchResults: Resource<List<Anime>> = Resource.Success(emptyList()),

    // Pagination
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
    val isLoadingMore: Boolean = false,

    // UI states
    val isSearching: Boolean = false,
    val showEmptyState: Boolean = false,
    val error: String? = null
) {
    /**
     * Check if has results
     */
    fun hasResults(): Boolean {
        return searchResults is Resource.Success &&
                searchResults.data?.isNotEmpty() == true
    }

    /**
     * Check if query is valid (min 2 characters)
     */
    fun isQueryValid(): Boolean {
        return query.trim().length >= 2
    }

    /**
     * Get results count
     */
    fun getResultsCount(): Int {
        return when (searchResults) {
            is Resource.Success -> searchResults.data?.size ?: 0
            else -> 0
        }
    }

    /**
     * Check if should show empty state
     */
    fun shouldShowEmptyState(): Boolean {
        return query.isNotEmpty() &&
                searchResults is Resource.Success &&
                searchResults.data?.isEmpty() == true
    }
}
