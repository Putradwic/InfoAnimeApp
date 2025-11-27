package com.putradwicahyono.ime.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.putradwicahyono.ime.di.AppModule
import com.putradwicahyono.ime.ui.theme.ImeTheme

/**
 * Main Activity
 * Entry point aplikasi dengan setup theme & navigation
 *
 * File: MainActivity.kt
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            // Get preferences untuk theme
            val preferences = AppModule.provideAppPreferences(this)
            val isDarkMode by preferences.isDarkMode.collectAsState(initial = false)

            // Setup theme
            ImeTheme(darkTheme = isDarkMode) {
                    // Setup navigation dengan Bottom Nav
                    MainScreen()

            }
        }
    }
}