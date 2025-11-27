package com.putradwicahyono.ime.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.putradwicahyono.ime.di.AppModule
import com.putradwicahyono.ime.di.NetworkModule
import com.putradwicahyono.ime.ui.detail.DetailScreen
import com.putradwicahyono.ime.ui.detail.DetailViewModel
import com.putradwicahyono.ime.ui.detail.DetailViewModelFactory
import com.putradwicahyono.ime.ui.home.HomeScreen
import com.putradwicahyono.ime.ui.home.HomeViewModel
import com.putradwicahyono.ime.ui.home.HomeViewModelFactory
import com.putradwicahyono.ime.ui.search.SearchScreen
import com.putradwicahyono.ime.ui.search.SearchViewModel
import com.putradwicahyono.ime.ui.search.SearchViewModelFactory
import com.putradwicahyono.ime.ui.seasonal.SeasonalScreen
import com.putradwicahyono.ime.ui.seasonal.SeasonalViewModel
import com.putradwicahyono.ime.ui.seasonal.SeasonalViewModelFactory
import com.putradwicahyono.ime.ui.top.TopScreen
import com.putradwicahyono.ime.ui.top.TopViewModel
import com.putradwicahyono.ime.ui.top.TopViewModelFactory
import com.putradwicahyono.ime.ui.upcoming.UpcomingScreen
import com.putradwicahyono.ime.ui.upcoming.UpcomingViewModel
import com.putradwicahyono.ime.ui.upcoming.UpcomingViewModelFactory

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    val context = LocalContext.current

    // Setup dependencies
    val apiService = NetworkModule.getApiService()
    val repository = AppModule.provideAnimeRepository(apiService, context)  // ← Pass context
    val preferences = AppModule.provideAppPreferences(context)

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // ==================== Home Screen ====================
        composable(route = Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository)
            )

            HomeScreen(
                viewModel = viewModel,
                onNavigateToSearch = {
                    navController.navigate(Screen.Search.route)
                },
                onNavigateToTop = {
                    navController.navigate(Screen.Top.route)
                },
                onNavigateToSeasonal = { year, season ->
                    navController.navigate(Screen.Seasonal.createRoute(year, season))
                },
                onNavigateToUpcoming = {
                    navController.navigate(Screen.Upcoming.route)
                },
                onNavigateToDetail = { animeId ->
                    navController.navigate(Screen.Detail.createRoute(animeId))
                }
            )
        }

        // ==================== Search Screen ====================
        composable(route = Screen.Search.route) {
            val viewModel: SearchViewModel = viewModel(
                factory = SearchViewModelFactory(repository)
            )

            SearchScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToDetail = { animeId ->
                    navController.navigate(Screen.Detail.createRoute(animeId))
                }
            )
        }

        // ==================== Top Screen ====================
        composable(route = Screen.Top.route) {
            val viewModel: TopViewModel = viewModel(
                factory = TopViewModelFactory(repository)
            )

            TopScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToDetail = { animeId ->
                    navController.navigate(Screen.Detail.createRoute(animeId))
                }
            )
        }

        // ==================== Seasonal Screen ====================
        composable(
            route = Screen.Seasonal.routeWithArgs,
            arguments = listOf(
                navArgument(NavArgs.YEAR) {
                    type = NavType.IntType
                },
                navArgument(NavArgs.SEASON) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val year = backStackEntry.arguments?.getInt(NavArgs.YEAR) ?: 2025
            val season = backStackEntry.arguments?.getString(NavArgs.SEASON) ?: "winter"

            val viewModel: SeasonalViewModel = viewModel(
                factory = SeasonalViewModelFactory(repository, year, season)
            )

            SeasonalScreen(
                viewModel = viewModel,
                initialYear = year,
                initialSeason = season,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToDetail = { animeId ->
                    navController.navigate(Screen.Detail.createRoute(animeId))
                }
            )
        }

        // ==================== Upcoming Screen ====================
        composable(route = Screen.Upcoming.route) {
            val viewModel: UpcomingViewModel = viewModel(
                factory = UpcomingViewModelFactory(repository)
            )

            UpcomingScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToDetail = { animeId ->
                    navController.navigate(Screen.Detail.createRoute(animeId))
                }
            )
        }

        // ==================== Detail Screen ====================
        composable(
            route = Screen.Detail.routeWithArgs,
            arguments = listOf(
                navArgument(NavArgs.ANIME_ID) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt(NavArgs.ANIME_ID) ?: 0

            val viewModel: DetailViewModel = viewModel(
                factory = DetailViewModelFactory(repository, animeId)
            )

            DetailScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
