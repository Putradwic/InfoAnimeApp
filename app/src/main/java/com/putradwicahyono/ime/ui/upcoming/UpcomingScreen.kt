package com.putradwicahyono.ime.ui.upcoming

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.putradwicahyono.ime.ui.components.*

/**
 * Upcoming Screen
 * Menampilkan upcoming anime dengan tab filter per year
 *
 * File: ui/upcoming/UpcomingScreen.kt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingScreen(
    viewModel: UpcomingViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val gridState = rememberLazyGridState()

    // Detect scroll to bottom untuk pagination
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = gridState.layoutInfo.totalItemsCount

            lastVisibleItem != null &&
                    lastVisibleItem.index >= totalItems - 3 &&
                    state.canLoadMore()
        }
    }

    // Trigger load more
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadMore()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Upcoming Anime")
                        if (state.hasData()) {
                            Text(
                                text = "${state.getAnimeCount()} anime in ${state.selectedYear}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Year Tabs
            if (state.availableYears.isNotEmpty()) {
                ScrollableTabRow(
                    selectedTabIndex = state.availableYears.indexOf(state.selectedYear),
                    modifier = Modifier.fillMaxWidth(),
                    edgePadding = 16.dp
                ) {
                    state.availableYears.forEach { year ->
                        Tab(
                            selected = state.selectedYear == year,
                            onClick = { viewModel.onYearSelected(year) },
                            text = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = year.toString(),
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    // Show count for each year
                                    val animeCount = state.allUpcomingAnime.count { it.year == year }
                                    if (animeCount > 0) {
                                        Text(
                                            text = "$animeCount anime",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (state.selectedYear == year) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                            }
                                        )
                                    }
                                }
                            }
                        )
                    }
                }

                Divider()
            }

            // Content
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    // Initial loading
                    state.isLoading && state.isInitialLoad -> {
                        LoadingIndicator(message = "Loading upcoming anime...")
                    }

                    // Error on initial load
                    state.error != null && state.allUpcomingAnime.isEmpty() -> {
                        ErrorState(
                            message = state.error ?: "Failed to load upcoming anime",
                            onRetry = { viewModel.retry() }
                        )
                    }

                    // Success with data
                    state.hasData() -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            state = gridState,
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Anime items
                            items(
                                items = state.displayedAnime,
                                key = { it.id }
                            ) { anime ->
                                AnimeCard(
                                    anime = anime,
                                    showRank = false,
                                    onClick = { onNavigateToDetail(anime.id) }
                                )
                            }

                            // Loading more indicator
                            if (state.isLoadingMore) {
                                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(32.dp),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }

                            // End of list message
                            if (!state.hasNextPage && state.hasData()) {
                                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 24.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "No more upcoming anime",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            // Error message saat load more gagal
                            if (state.error != null && state.allUpcomingAnime.isNotEmpty()) {
                                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = state.error ?: "Failed to load more",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        TextButton(onClick = { viewModel.loadMore() }) {
                                            Text("Retry")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Empty state untuk selected year
                    !state.hasData() && !state.isLoading && state.allUpcomingAnime.isNotEmpty() -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "📅",
                                style = MaterialTheme.typography.displayLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No anime for ${state.selectedYear}",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Try selecting a different year",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Empty state (no data at all)
                    !state.hasData() && !state.isLoading && state.allUpcomingAnime.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "📺",
                                style = MaterialTheme.typography.displayLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No upcoming anime",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
