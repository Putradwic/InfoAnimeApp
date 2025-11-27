package com.putradwicahyono.ime.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.putradwicahyono.ime.ui.navigation.NavGraph
import com.putradwicahyono.ime.ui.navigation.Screen
import com.putradwicahyono.ime.util.SeasonUtil

/**
 * Main Screen with Bottom Navigation
 * Container utama aplikasi dengan Bottom Navigation Bar
 *
 * File: ui/MainScreen.kt
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Daftar screen yang menampilkan bottom navigation
    val bottomNavRoutes = listOf(
        Screen.Home.route,
        Screen.Search.route,
        Screen.Top.route,
        Screen.Seasonal.route,
        Screen.Upcoming.route
    )

    // Cek apakah current screen harus menampilkan bottom nav
    val shouldShowBottomNav = currentRoute in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (shouldShowBottomNav) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) {
        NavGraph(
            navController = navController,
            startDestination = Screen.Home.route
        )
    }
}

/**
 * Bottom Navigation Bar Component
 */
@Composable
private fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            // Untuk seasonal, cek apakah current route adalah seasonal
            val isSelected = if (item.route == "seasonal") {
                currentRoute?.startsWith("seasonal/") == true
            } else {
                currentRoute == item.route
            }

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = {
                    // Untuk seasonal, navigate dengan current season
                    val targetRoute = if (item.route == "seasonal") {
                        val currentYear = SeasonUtil.getCurrentYear()
                        val currentSeason = SeasonUtil.getCurrentSeason()  // ← Sudah return String
                        Screen.Seasonal.createRoute(currentYear, currentSeason)
                    } else {
                        item.route
                    }

                    if (currentRoute != targetRoute) {
                        navController.navigate(targetRoute) {
                            // Pop up to home to avoid building large back stack
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            // Avoid multiple copies of same destination
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                }
            )
        }
    }

}

/**
 * Bottom Navigation Items Data Class
 */
private data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

/**
 * List Bottom Navigation Items
 */
private val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Home.route,
        icon = Icons.Default.Home,
        label = "Home"
    ),
    BottomNavItem(
        route = Screen.Search.route,
        icon = Icons.Default.Search,
        label = "Search"
    ),
    BottomNavItem(
        route = Screen.Top.route,
        icon = Icons.Default.Star,
        label = "Top"
    ),
    BottomNavItem(
        route = "seasonal",
        icon = Icons.Default.DateRange,
        label = "Seasonal"
    ),
    BottomNavItem(
        route = Screen.Upcoming.route,
        icon = Icons.Default.Notifications,
        label = "Upcoming"
    )
)