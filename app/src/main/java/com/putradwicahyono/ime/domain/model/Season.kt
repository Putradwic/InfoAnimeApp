package com.putradwicahyono.ime.domain.model

/**
 * Season Enum Class
 * Representasi musim anime di Jepang
 *
 * File: domain/model/Season.kt
 */
enum class Season(
    val apiName: String,
    val displayNameEN: String,
    val displayNameID: String,
    val emoji: String,
    val months: String
) {
    WINTER(
        apiName = "winter",
        displayNameEN = "Winter",
        displayNameID = "Musim Dingin",
        emoji = "❄️",
        months = "Dec-Feb"
    ),
    SPRING(
        apiName = "spring",
        displayNameEN = "Spring",
        displayNameID = "Musim Semi",
        emoji = "🌸",
        months = "Mar-May"
    ),
    SUMMER(
        apiName = "summer",
        displayNameEN = "Summer",
        displayNameID = "Musim Panas",
        emoji = "☀️",
        months = "Jun-Aug"
    ),
    FALL(
        apiName = "fall",
        displayNameEN = "Fall",
        displayNameID = "Musim Gugur",
        emoji = "🍂",
        months = "Sep-Nov"
    );

    /**
     * Get display name berdasarkan language
     * @param languageCode "en" atau "id"
     */
    fun getDisplayName(languageCode: String = "en"): String {
        return if (languageCode == "id") displayNameID else displayNameEN
    }

    /**
     * Get display name dengan emoji
     */
    fun getDisplayNameWithEmoji(languageCode: String = "en"): String {
        return "$emoji ${getDisplayName(languageCode)}"
    }

    /**
     * Get previous season
     */
    fun previous(): Season {
        return when (this) {
            WINTER -> FALL
            SPRING -> WINTER
            SUMMER -> SPRING
            FALL -> SUMMER
        }
    }

    /**
     * Get next season
     */
    fun next(): Season {
        return when (this) {
            WINTER -> SPRING
            SPRING -> SUMMER
            SUMMER -> FALL
            FALL -> WINTER
        }
    }

    companion object {
        /**
         * Get Season from API name string
         * @param apiName "winter", "spring", "summer", "fall"
         * @return Season enum atau null
         */
        fun fromApiName(apiName: String?): Season? {
            return values().find { it.apiName.equals(apiName, ignoreCase = true) }
        }

        /**
         * Get current season berdasarkan month
         * @param month 1-12
         */
        fun fromMonth(month: Int): Season {
            return when (month) {
                3, 4, 5 -> SPRING
                6, 7, 8 -> SUMMER
                9, 10, 11 -> FALL
                12, 1, 2 -> WINTER
                else -> WINTER
            }
        }

        /**
         * Get all seasons sebagai list
         */
        fun getAllSeasons(): List<Season> {
            return values().toList()
        }

        /**
         * Get all API names
         */
        fun getAllApiNames(): List<String> {
            return values().map { it.apiName }
        }
    }
}

/**
 * Data class untuk Season dengan Year
 * Untuk filter di Seasonal Screen
 */
data class SeasonYear(
    val season: Season,
    val year: Int
) {
    /**
     * Get display string
     * Returns: "Winter 2025"
     */
    fun getDisplayString(languageCode: String = "en"): String {
        return "${season.getDisplayName(languageCode)} $year"
    }

    /**
     * Get display string dengan emoji
     * Returns: "❄️ Winter 2025"
     */
    fun getDisplayStringWithEmoji(languageCode: String = "en"): String {
        return "${season.emoji} ${season.getDisplayName(languageCode)} $year"
    }

    /**
     * Get previous season year
     * Handle: Winter 2025 → Fall 2024
     */
    fun previous(): SeasonYear {
        return if (season == Season.WINTER) {
            SeasonYear(Season.FALL, year - 1)
        } else {
            SeasonYear(season.previous(), year)
        }
    }

    /**
     * Get next season year
     * Handle: Fall 2025 → Winter 2026
     */
    fun next(): SeasonYear {
        return if (season == Season.FALL) {
            SeasonYear(Season.WINTER, year + 1)
        } else {
            SeasonYear(season.next(), year)
        }
    }

    companion object {
        /**
         * Get current season year (realtime)
         */
        fun current(): SeasonYear {
            val now = java.time.LocalDate.now()
            val currentMonth = now.monthValue
            val currentYear = now.year

            val season = Season.fromMonth(currentMonth)
            return SeasonYear(season, currentYear)
        }

        /**
         * Get previous season year (realtime)
         */
        fun previousFromCurrent(): SeasonYear {
            return current().previous()
        }

        /**
         * Get next season year (realtime)
         */
        fun nextFromCurrent(): SeasonYear {
            return current().next()
        }
    }
}

/**
 * Extension function untuk List<Season>
 */
fun List<Season>.getDisplayNames(languageCode: String = "en"): List<String> {
    return this.map { it.getDisplayName(languageCode) }
}

fun List<Season>.getApiNames(): List<String> {
    return this.map { it.apiName }
}