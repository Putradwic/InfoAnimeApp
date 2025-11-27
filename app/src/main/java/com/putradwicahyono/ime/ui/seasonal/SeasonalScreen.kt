package com.putradwicahyono.ime.ui.seasonal

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
import com.putradwicahyono.ime.util.Constants

/**
 * Seasonal Screen
 * Menampilkan seasonal anime dengan filter year & season
 *
 * File: ui/seasonal/SeasonalScreen.kt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonalScreen(
    viewModel: SeasonalViewModel,
    initialYear: Int,
    initialSeason: String,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val gridState = rememberLazyGridState()

    // Dropdown states
    var yearDropdownExpanded by remember { mutableStateOf(false) }

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
                        Text("Seasonal Anime")
                        Text(
                            text = state.getDisplayTitle(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
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
            // Filter Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Filter",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Year Dropdown
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { yearDropdownExpanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(state.selectedYear.toString())
                            }

                            DropdownMenu(
                                expanded = yearDropdownExpanded,
                                onDismissRequest = { yearDropdownExpanded = false }
                            ) {
                                state.availableYears.forEach { year ->
                                    DropdownMenuItem(
                                        text = { Text(year.toString()) },
                                        onClick = {
                                            viewModel.onYearSelected(year)
                                            yearDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Season Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val seasons = listOf(
                            Constants.Season.WINTER,
                            Constants.Season.SPRING,
                            Constants.Season.SUMMER,
                            Constants.Season.FALL
                        )

                        seasons.forEach { season ->
                            FilterChip(
                                selected = state.selectedSeason == season,
                                onClick = { viewModel.onSeasonSelected(season) },
                                label = {
                                    Text(
                                        text = season.replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase() else it.toString()
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // Content
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    // Initial loading
                    state.isLoading && state.isInitialLoad -> {
                        LoadingIndicator(message = "Loading seasonal anime...")
                    }

                    // Error on initial load
                    state.error != null && !state.hasData() -> {
                        ErrorState(
                            message = state.error ?: "Failed to load seasonal anime",
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
                                items = state.seasonalAnime,
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
                                            text = "No more anime for this season",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            // Error message saat load more gagal
                            if (state.error != null && state.hasData()) {
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

                    // Empty state
                    !state.hasData() && !state.isLoading -> {
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
                                text = "No anime for this season",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Try selecting a different season or year",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
