package com.putradwicahyono.ime.ui.navigation

/**
 * Screen Sealed Class
 * Define semua routes untuk navigation
 *
 * File: ui/navigation/Screen.kt
 */
sealed class Screen(val route: String) {

    /**
     * Home Screen
     * Route: "home"
     */
    object Home : Screen("home")

    /**
     * Search Screen
     * Route: "search"
     */
    object Search : Screen("search")

    /**
     * Top Anime Screen
     * Route: "top"
     */
    object Top : Screen("top")

    /**
     * Seasonal Anime Screen
     * Route: "seasonal/{year}/{season}"
     *
     * Arguments:
     * - year: Int (contoh: 2025)
     * - season: String (winter, spring, summer, fall)
     */
    object Seasonal : Screen("seasonal/{year}/{season}") {
        /**
         * Create route dengan arguments
         */
        fun createRoute(year: Int, season: String): String {
            return "seasonal/$year/$season"
        }

        /**
         * Route tanpa arguments (untuk NavHost)
         */
        const val routeWithArgs = "seasonal/{year}/{season}"
    }

    /**
     * Upcoming Anime Screen
     * Route: "upcoming"
     */
    object Upcoming : Screen("upcoming")

    /**
     * Detail Screen (Future feature)
     * Route: "detail/{animeId}"
     *
     * Arguments:
     * - animeId: Int (MAL ID)
     */
    object Detail : Screen("detail/{animeId}") {
        /**
         * Create route dengan arguments
         */
        fun createRoute(animeId: Int): String {
            return "detail/$animeId"
        }

        /**
         * Route tanpa arguments (untuk NavHost)
         */
        const val routeWithArgs = "detail/{animeId}"
    }

}

/**
 * Navigation Arguments Keys
 */
object NavArgs {
    const val ANIME_ID = "animeId"
    const val YEAR = "year"
    const val SEASON = "season"
}