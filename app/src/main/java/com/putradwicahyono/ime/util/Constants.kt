package com.putradwicahyono.ime.util

/**
 * Constants Object
 * Menyimpan semua konstanta aplikasi
 *
 * File: util/Constants.kt
 */
object Constants {

    /**
     * Base URL Jikan API
     */
    const val BASE_URL = "https://api.jikan.moe/v4/"

    /**
     * Pagination Constants
     */
    const val DEFAULT_PAGE_SIZE = 10
    const val TOP_ANIME_PAGE_SIZE = 10
    const val SEASONAL_PAGE_SIZE = 10
    const val UPCOMING_PAGE_SIZE = 25
    const val SEARCH_PAGE_SIZE = 10

    /**
     * Home Screen Constants
     */
    const val TOP_ANIME_INITIAL_COUNT = 6  // 3 baris x 2 kolom
    const val TOP_ANIME_EXPANDED_COUNT = 10 // Setelah expand
    const val SEASONAL_ANIME_COUNT = 10    // Horizontal scroll
    const val UPCOMING_ANIME_COUNT = 10    // Horizontal scroll

    /**
     * Image Placeholder
     */
    const val PLACEHOLDER_IMAGE_URL = "https://via.placeholder.com/300x450"

    /**
     * Season Names
     */
    object Season {
        const val WINTER = "winter"   // Dec, Jan, Feb
        const val SPRING = "spring"   // Mar, Apr, May
        const val SUMMER = "summer"   // Jun, Jul, Aug
        const val FALL = "fall"       // Sep, Oct, Nov
    }

    /**
     * Language Codes
     */
    object Language {
        const val ENGLISH = "en"
        const val INDONESIAN = "id"
    }

    /**
     * Theme Mode
     */
    object Theme {
        const val LIGHT = "light"
        const val DARK = "dark"
        const val SYSTEM = "system"
    }

    /**
     * Navigation Routes
     */
    object Routes {
        const val HOME = "home"
        const val SEARCH = "search"
        const val TOP = "top"
        const val SEASONAL = "seasonal"
        const val UPCOMING = "upcoming"
        const val DETAIL = "detail"
        const val SETTINGS = "settings"

        // With arguments
        const val DETAIL_WITH_ID = "detail/{animeId}"
        const val SEASONAL_WITH_FILTER = "seasonal/{year}/{season}"

        // Navigation helpers
        fun detailRoute(animeId: Int) = "detail/$animeId"
        fun seasonalRoute(year: Int, season: String) = "seasonal/$year/$season"
    }

    /**
     * Animation Duration
     */
    const val ANIMATION_DURATION = 300

    /**
     * Debounce Delay (untuk search)
     */
    const val SEARCH_DEBOUNCE_DELAY = 500L // milliseconds

    /**
     * Network Timeout
     */
    const val NETWORK_TIMEOUT = 30L // seconds
}