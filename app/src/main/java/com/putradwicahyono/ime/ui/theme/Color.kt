package com.putradwicahyono.ime.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Color Palette untuk Light & Dark Theme
 * Material Design 3 Color System
 *
 * File: ui/theme/Color.kt
 */

// ==================== Light Theme Colors ====================

val LightPrimary = Color(0xFF6750A4)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFEADDFF)
val LightOnPrimaryContainer = Color(0xFF21005D)

val LightSecondary = Color(0xFF625B71)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFE8DEF8)
val LightOnSecondaryContainer = Color(0xFF1D192B)

val LightTertiary = Color(0xFF7D5260)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFFFFD8E4)
val LightOnTertiaryContainer = Color(0xFF31111D)

val LightError = Color(0xFFB3261E)
val LightOnError = Color(0xFFFFFFFF)
val LightErrorContainer = Color(0xFFF9DEDC)
val LightOnErrorContainer = Color(0xFF410E0B)

val LightBackground = Color(0xFFFFFBFE)
val LightOnBackground = Color(0xFF1C1B1F)
val LightSurface = Color(0xFFFFFBFE)
val LightOnSurface = Color(0xFF1C1B1F)

val LightSurfaceVariant = Color(0xFFE7E0EC)
val LightOnSurfaceVariant = Color(0xFF49454F)
val LightOutline = Color(0xFF79747E)
val LightOutlineVariant = Color(0xFFCAC4D0)

val LightScrim = Color(0xFF000000)
val LightInverseSurface = Color(0xFF313033)
val LightInverseOnSurface = Color(0xFFF4EFF4)
val LightInversePrimary = Color(0xFFD0BCFF)

// ==================== Dark Theme Colors ====================

val DarkPrimary = Color(0xFFD0BCFF)
val DarkOnPrimary = Color(0xFF381E72)
val DarkPrimaryContainer = Color(0xFF4F378B)
val DarkOnPrimaryContainer = Color(0xFFEADDFF)

val DarkSecondary = Color(0xFFCCC2DC)
val DarkOnSecondary = Color(0xFF332D41)
val DarkSecondaryContainer = Color(0xFF4A4458)
val DarkOnSecondaryContainer = Color(0xFFE8DEF8)

val DarkTertiary = Color(0xFFEFB8C8)
val DarkOnTertiary = Color(0xFF492532)
val DarkTertiaryContainer = Color(0xFF633B48)
val DarkOnTertiaryContainer = Color(0xFFFFD8E4)

val DarkError = Color(0xFFF2B8B5)
val DarkOnError = Color(0xFF601410)
val DarkErrorContainer = Color(0xFF8C1D18)
val DarkOnErrorContainer = Color(0xFFF9DEDC)

val DarkBackground = Color(0xFF1C1B1F)
val DarkOnBackground = Color(0xFFE6E1E5)
val DarkSurface = Color(0xFF1C1B1F)
val DarkOnSurface = Color(0xFFE6E1E5)

val DarkSurfaceVariant = Color(0xFF49454F)
val DarkOnSurfaceVariant = Color(0xFFCAC4D0)
val DarkOutline = Color(0xFF938F99)
val DarkOutlineVariant = Color(0xFF49454F)

val DarkScrim = Color(0xFF000000)
val DarkInverseSurface = Color(0xFFE6E1E5)
val DarkInverseOnSurface = Color(0xFF313033)
val DarkInversePrimary = Color(0xFF6750A4)

// ==================== Custom Colors (Anime App Specific) ====================

// Score colors
val ScoreExcellent = Color(0xFF4CAF50)  // Green for 8.0+
val ScoreGood = Color(0xFF2196F3)       // Blue for 7.0-7.9
val ScoreAverage = Color(0xFFFF9800)    // Orange for 6.0-6.9
val ScorePoor = Color(0xFFF44336)       // Red for below 6.0

// Status colors
val StatusAiring = Color(0xFF2196F3)    // Blue
val StatusFinished = Color(0xFF4CAF50)  // Green
val StatusUpcoming = Color(0xFFFF9800)  // Orange

// Rating colors (age rating)
val RatingG = Color(0xFF4CAF50)         // Green
val RatingPG = Color(0xFF2196F3)        // Blue
val RatingPG13 = Color(0xFFFF9800)      // Orange
val RatingR = Color(0xFFF44336)         // Red
val RatingRPlus = Color(0xFFE91E63)     // Pink
val RatingRx = Color(0xFF9C27B0)        // Purple

// Type colors
val TypeTV = Color(0xFF2196F3)          // Blue
val TypeMovie = Color(0xFFF44336)       // Red
val TypeOVA = Color(0xFF9C27B0)         // Purple
val TypeONA = Color(0xFF4CAF50)         // Green
val TypeSpecial = Color(0xFFFF9800)     // Orange
val TypeMusic = Color(0xFFE91E63)       // Pink

// Season colors
val SeasonWinter = Color(0xFF2196F3)    // Blue
val SeasonSpring = Color(0xFFE91E63)    // Pink
val SeasonSummer = Color(0xFFFF9800)    // Orange
val SeasonFall = Color(0xFF795548)      // Brown

// Rank badge colors
val RankGold = Color(0xFFFFD700)        // #1
val RankSilver = Color(0xFFC0C0C0)      // #2
val RankBronze = Color(0xFFCD7F32)      // #3
val RankDefault = Color(0xFF757575)     // #4+

// Gradient colors (for hero section)
val GradientStart = Color(0x00000000)   // Transparent
val GradientEnd = Color(0xCC000000)     // Semi-transparent black

// Card colors
val CardBackgroundLight = Color(0xFFFFFFFF)
val CardBackgroundDark = Color(0xFF2C2C2C)

// Shimmer effect colors
val ShimmerLight = Color(0xFFE0E0E0)
val ShimmerDark = Color(0xFF3C3C3C)

// ==================== Helper Functions ====================

/**
 * Get score color berdasarkan score value
 */
fun getScoreColor(score: Double?): Color {
    return when {
        score == null -> Color.Gray
        score >= 8.0 -> ScoreExcellent
        score >= 7.0 -> ScoreGood
        score >= 6.0 -> ScoreAverage
        else -> ScorePoor
    }
}

/**
 * Get status color berdasarkan status string
 */
fun getStatusColor(status: String?): Color {
    return when (status?.lowercase()) {
        "currently airing" -> StatusAiring
        "finished airing" -> StatusFinished
        "not yet aired" -> StatusUpcoming
        else -> Color.Gray
    }
}

/**
 * Get rating color berdasarkan age rating
 */
fun getRatingColor(rating: String?): Color {
    return when (rating?.uppercase()) {
        "G" -> RatingG
        "PG" -> RatingPG
        "PG-13", "PG13" -> RatingPG13
        "R" -> RatingR
        "R+" -> RatingRPlus
        "RX" -> RatingRx
        else -> Color.Gray
    }
}

/**
 * Get type color berdasarkan anime type
 */
fun getTypeColor(type: String?): Color {
    return when (type?.uppercase()) {
        "TV" -> TypeTV
        "MOVIE" -> TypeMovie
        "OVA" -> TypeOVA
        "ONA" -> TypeONA
        "SPECIAL" -> TypeSpecial
        "MUSIC" -> TypeMusic
        else -> Color.Gray
    }
}

/**
 * Get season color berdasarkan season name
 */
fun getSeasonColor(season: String?): Color {
    return when (season?.lowercase()) {
        "winter" -> SeasonWinter
        "spring" -> SeasonSpring
        "summer" -> SeasonSummer
        "fall" -> SeasonFall
        else -> Color.Gray
    }
}

/**
 * Get rank badge color berdasarkan rank number
 */
fun getRankColor(rank: Int?): Color {
    return when (rank) {
        1 -> RankGold
        2 -> RankSilver
        3 -> RankBronze
        else -> RankDefault
    }
}

