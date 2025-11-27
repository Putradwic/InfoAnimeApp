package com.putradwicahyono.ime.util

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

/**
 * Extension Functions
 * Helper functions untuk berbagai kebutuhan
 *
 * File: util/Extensions.kt
 */

// ==================== Context Extensions ====================

/**
 * Show Toast message
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Get string resource by ID
 */
fun Context.getString(resId: Int, vararg formatArgs: Any): String {
    return this.getString(resId, *formatArgs)
}

// ==================== String Extensions ====================

/**
 * Truncate string dengan max length
 * Tambahkan "..." jika melebihi
 */
fun String.truncate(maxLength: Int): String {
    return if (this.length > maxLength) {
        "${this.substring(0, maxLength)}..."
    } else {
        this
    }
}

/**
 * Capitalize first letter
 */
fun String.capitalizeFirst(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}

/**
 * Check if string is valid URL
 */
fun String.isValidUrl(): Boolean {
    return this.startsWith("http://") || this.startsWith("https://")
}

// ==================== Number Extensions ====================

/**
 * Format number dengan separator (1000 -> 1,000)
 */
fun Int.formatWithComma(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}

/**
 * Format number ke K/M format (1000 -> 1K, 1000000 -> 1M)
 */
fun Int.formatToShort(): String {
    return when {
        this >= 1_000_000 -> String.format("%.1fM", this / 1_000_000.0)
        this >= 1_000 -> String.format("%.1fK", this / 1_000.0)
        else -> this.toString()
    }
}

/**
 * Format score (8.5 -> 8.5, 8.0 -> 8.0, null -> N/A)
 */
fun Double?.formatScore(): String {
    return this?.let { String.format("%.1f", it) } ?: "N/A"
}

// ==================== List Extensions ====================

/**
 * Get first N items dari list
 */
fun <T> List<T>.takeOrAll(count: Int): List<T> {
    return if (this.size > count) this.take(count) else this
}

/**
 * Chunk list menjadi groups
 * Contoh: [1,2,3,4,5].chunked(2) -> [[1,2], [3,4], [5]]
 */
fun <T> List<T>.chunkedList(size: Int): List<List<T>> {
    return this.chunked(size)
}

// ==================== Season Extensions ====================

/**
 * Get season emoji
 */
fun String.getSeasonEmoji(): String {
    return when (this.lowercase()) {
        "winter" -> "❄️"
        "spring" -> "🌸"
        "summer" -> "☀️"
        "fall" -> "🍂"
        else -> "📺"
    }
}

/**
 * Get anime type emoji
 */
fun String.getTypeEmoji(): String {
    return when (this.uppercase()) {
        "TV" -> "📺"
        "MOVIE" -> "🎬"
        "OVA" -> "💿"
        "ONA" -> "🌐"
        "SPECIAL" -> "⭐"
        "MUSIC" -> "🎵"
        else -> "📺"
    }
}

// ==================== Composable Extensions ====================

/**
 * Get context dalam Composable
 */
@Composable
fun rememberContext(): Context {
    return LocalContext.current
}

/**
 * Show toast dari Composable
 */
@Composable
fun ShowToast(message: String) {
    val context = LocalContext.current
    context.showToast(message)
}

// ==================== Flow/Coroutine Extensions ====================

/**
 * Debounce function untuk search
 * Delay execution hingga user berhenti typing
 */
fun <T> debounce(
    delayMillis: Long = Constants.SEARCH_DEBOUNCE_DELAY,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: kotlinx.coroutines.Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = kotlinx.coroutines.CoroutineScope(
            kotlinx.coroutines.Dispatchers.Main
        ).launch {
            kotlinx.coroutines.delay(delayMillis)
            action(param)
        }
    }
}

// ==================== Anime-specific Extensions ====================

/**
 * Get anime rating color (untuk badge)
 */
fun String?.getRatingColor(): androidx.compose.ui.graphics.Color {
    return when (this?.uppercase()) {
        "G" -> androidx.compose.ui.graphics.Color(0xFF4CAF50) // Green
        "PG" -> androidx.compose.ui.graphics.Color(0xFF2196F3) // Blue
        "PG-13" -> androidx.compose.ui.graphics.Color(0xFFFF9800) // Orange
        "R", "R+" -> androidx.compose.ui.graphics.Color(0xFFF44336) // Red
        "RX" -> androidx.compose.ui.graphics.Color(0xFF9C27B0) // Purple
        else -> androidx.compose.ui.graphics.Color.Gray
    }
}

/**
 * Get status color
 */
fun String?.getStatusColor(): androidx.compose.ui.graphics.Color {
    return when (this?.lowercase()) {
        "finished airing" -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
        "currently airing" -> androidx.compose.ui.graphics.Color(0xFF2196F3)
        "not yet aired" -> androidx.compose.ui.graphics.Color(0xFFFF9800)
        else -> androidx.compose.ui.graphics.Color.Gray
    }
}