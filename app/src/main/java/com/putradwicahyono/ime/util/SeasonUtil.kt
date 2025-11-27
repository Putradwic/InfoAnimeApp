package com.putradwicahyono.ime.util

import java.time.LocalDate
import java.time.Month

/**
 * Season Utility
 * Helper untuk calculate current & previous season
 *
 * File: util/SeasonUtil.kt
 */
object SeasonUtil {

    /**
     * Get current season berdasarkan bulan saat ini
     *
     * Aturan musim anime di Jepang:
     * - Winter: December, January, February
     * - Spring: March, April, May
     * - Summer: June, July, August
     * - Fall: September, October, November
     *
     * @return Season name (winter, spring, summer, fall)
     */
    fun getCurrentSeason(): String {
        val currentMonth = LocalDate.now().month

        return when (currentMonth) {
            Month.MARCH, Month.APRIL, Month.MAY -> Constants.Season.SPRING
            Month.JUNE, Month.JULY, Month.AUGUST -> Constants.Season.SUMMER
            Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER -> Constants.Season.FALL
            Month.DECEMBER, Month.JANUARY, Month.FEBRUARY -> Constants.Season.WINTER
            else -> Constants.Season.WINTER
        }
    }

    /**
     * Get previous season
     *
     * @param currentSeason Current season (optional, auto-detect jika null)
     * @return Previous season name
     */
    fun getPreviousSeason(currentSeason: String? = null): String {
        val season = currentSeason ?: getCurrentSeason()

        return when (season) {
            Constants.Season.WINTER -> Constants.Season.FALL
            Constants.Season.SPRING -> Constants.Season.WINTER
            Constants.Season.SUMMER -> Constants.Season.SPRING
            Constants.Season.FALL -> Constants.Season.SUMMER
            else -> Constants.Season.FALL
        }
    }

    /**
     * Get next season
     *
     * @param currentSeason Current season (optional, auto-detect jika null)
     * @return Next season name
     */
    fun getNextSeason(currentSeason: String? = null): String {
        val season = currentSeason ?: getCurrentSeason()

        return when (season) {
            Constants.Season.WINTER -> Constants.Season.SPRING
            Constants.Season.SPRING -> Constants.Season.SUMMER
            Constants.Season.SUMMER -> Constants.Season.FALL
            Constants.Season.FALL -> Constants.Season.WINTER
            else -> Constants.Season.SPRING
        }
    }

    /**
     * Get current year
     *
     * @return Current year (contoh: 2025)
     */
    fun getCurrentYear(): Int {
        return LocalDate.now().year
    }

    /**
     * Get year untuk previous season
     * Handle case: jika current season adalah Winter (Jan-Feb),
     * previous season (Fall) ada di tahun sebelumnya
     *
     * @return Year for previous season
     */
    fun getPreviousSeasonYear(): Int {
        val currentMonth = LocalDate.now().month
        val currentYear = getCurrentYear()

        // Jika bulan Jan atau Feb (Winter), previous season (Fall) ada di tahun lalu
        return if (currentMonth == Month.JANUARY || currentMonth == Month.FEBRUARY) {
            currentYear - 1
        } else {
            currentYear
        }
    }

    /**
     * Get list of years untuk upcoming anime
     * Biasanya 2-3 tahun ke depan
     *
     * @param count Jumlah tahun (default: 3)
     * @return List of years [2025, 2026, 2027]
     */
    fun getUpcomingYears(count: Int = 3): List<Int> {
        val currentYear = getCurrentYear()
        return List(count) { currentYear + it }
    }

    /**
     * Get all seasons dalam urutan
     *
     * @return List of all seasons
     */
    fun getAllSeasons(): List<String> {
        return listOf(
            Constants.Season.WINTER,
            Constants.Season.SPRING,
            Constants.Season.SUMMER,
            Constants.Season.FALL
        )
    }

    /**
     * Get season display name (untuk UI)
     *
     * @param season Season code (winter, spring, summer, fall)
     * @param languageCode Language code (en atau id)
     * @return Display name
     */
    fun getSeasonDisplayName(season: String, languageCode: String = "en"): String {
        return if (languageCode == "id") {
            when (season) {
                Constants.Season.WINTER -> "Musim Dingin"
                Constants.Season.SPRING -> "Musim Semi"
                Constants.Season.SUMMER -> "Musim Panas"
                Constants.Season.FALL -> "Musim Gugur"
                else -> season.capitalize()
            }
        } else {
            season.replaceFirstChar { it.uppercase() }
        }
    }

    /**
     * Helper: Capitalize first letter
     */
    private fun String.capitalize(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}