package com.putradwicahyono.ime.domain.model

/**
 * Anime Domain Model
 * Model bersih untuk UI layer (tanpa anotasi JSON/Database)
 *
 * File: domain/model/Anime.kt
 */
data class Anime(
    // Basic Info
    val id: Int,
    val title: String,
    val titleEnglish: String? = null,
    val titleJapanese: String? = null,
    val imageUrl: String,

    // Type & Status
    val type: String, // TV, Movie, OVA, Special, ONA, Music
    val episodes: Int? = null,
    val status: String? = null, // Finished Airing, Currently Airing, Not yet aired

    // Rating & Popularity
    val score: Double? = null,
    val rank: Int? = null,
    val popularity: Int? = null,

    // Description
    val synopsis: String? = null,

    // Seasonal Info
    val year: Int? = null,
    val season: String? = null, // winter, spring, summer, fall

    // Additional Info
    val rating: String? = null, // G, PG, PG-13, R, R+, Rx
    val duration: String? = null,

    // Collections
    val genres: List<String> = emptyList(),
    val studios: List<String> = emptyList()
) {
    /**
     * Get display title
     * Priority: titleEnglish → title
     */
    fun getDisplayTitle(preferEnglish: Boolean = true): String {
        return if (preferEnglish && !titleEnglish.isNullOrBlank()) {
            titleEnglish
        } else {
            title
        }
    }

    /**
     * Get formatted score
     * Returns: "8.5" atau "N/A"
     */
    fun getFormattedScore(): String {
        return score?.let { String.format("%.1f", it) } ?: "N/A"
    }

    /**
     * Get formatted rank
     * Returns: "#1" atau null
     */
    fun getFormattedRank(): String? {
        return rank?.let { "#$it" }
    }

    /**
     * Get formatted type
     * Returns: "TV" atau "Movie" atau "Unknown"
     */
    fun getFormattedType(): String {
        return type.ifBlank { "Unknown" }
    }

    /**
     * Get formatted episodes
     * Returns: "12 eps" atau "? eps" atau null
     */
    fun getFormattedEpisodes(): String? {
        return episodes?.let { "$it eps" } ?: "? eps"
    }

    /**
     * Get year and season string
     * Returns: "Winter 2025" atau "2025" atau null
     */
    fun getSeasonYear(): String? {
        return when {
            year != null && season != null -> {
                "${season.replaceFirstChar { it.uppercase() }} $year"
            }
            year != null -> year.toString()
            else -> null
        }
    }

    /**
     * Get first genre atau null
     */
    fun getPrimaryGenre(): String? {
        return genres.firstOrNull()
    }

    /**
     * Get all genres as comma-separated string
     * Returns: "Action, Adventure, Fantasy" atau empty string
     */
    fun getGenresString(): String {
        return genres.joinToString(", ")
    }

    /**
     * Get first studio atau null
     */
    fun getPrimaryStudio(): String? {
        return studios.firstOrNull()
    }

    /**
     * Check if anime is currently airing
     */
    fun isCurrentlyAiring(): Boolean {
        return status?.lowercase()?.contains("currently airing") == true
    }

    /**
     * Check if anime is finished
     */
    fun isFinished(): Boolean {
        return status?.lowercase()?.contains("finished") == true
    }

    /**
     * Check if anime is upcoming
     */
    fun isUpcoming(): Boolean {
        return status?.lowercase()?.contains("not yet aired") == true
    }

    /**
     * Check if has valid image
     */
    fun hasValidImage(): Boolean {
        return imageUrl.isNotBlank() &&
                (imageUrl.startsWith("http://") || imageUrl.startsWith("https://"))
    }

    /**
     * Get truncated synopsis
     * @param maxLength Maximum length (default: 150)
     */
    fun getTruncatedSynopsis(maxLength: Int = 150): String {
        return if (synopsis.isNullOrBlank()) {
            "No synopsis available."
        } else if (synopsis.length > maxLength) {
            "${synopsis.substring(0, maxLength)}..."
        } else {
            synopsis
        }
    }
}

/**
 * Extension function untuk filter anime by year
 */
fun List<Anime>.filterByYear(year: Int): List<Anime> {
    return this.filter { it.year == year }
}

/**
 * Extension function untuk filter anime by season
 */
fun List<Anime>.filterBySeason(season: String): List<Anime> {
    return this.filter { it.season?.lowercase() == season.lowercase() }
}

/**
 * Extension function untuk sort by score
 */
fun List<Anime>.sortByScore(): List<Anime> {
    return this.sortedByDescending { it.score }
}

/**
 * Extension function untuk sort by rank
 */
fun List<Anime>.sortByRank(): List<Anime> {
    return this.sortedBy { it.rank }
}

/**
 * Extension function untuk sort by popularity
 */
fun List<Anime>.sortByPopularity(): List<Anime> {
    return this.sortedBy { it.popularity }
}