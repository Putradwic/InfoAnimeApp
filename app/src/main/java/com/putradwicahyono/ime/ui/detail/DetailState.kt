package com.putradwicahyono.ime.ui.detail

import com.putradwicahyono.ime.domain.model.Anime
import com.putradwicahyono.ime.util.Resource

/**
 * Detail State
 * UI State untuk Detail Screen
 *
 * File: ui/detail/DetailState.kt
 */
data class DetailState(
    // Anime detail
    val anime: Resource<Anime> = Resource.Loading(),

    // UI states
    val isLoading: Boolean = true,
    val error: String? = null
) {
    /**
     * Check if has data
     */
    fun hasData(): Boolean {
        return anime is Resource.Success && anime.data != null
    }

    /**
     * Get anime data
     */
    fun getAnime(): Anime? {
        return (anime as? Resource.Success)?.data
    }
}
