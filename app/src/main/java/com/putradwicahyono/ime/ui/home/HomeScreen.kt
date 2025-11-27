package com.putradwicahyono.ime.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.putradwicahyono.ime.ui.components.*
import com.putradwicahyono.ime.util.Resource
import kotlinx.coroutines.launch

/**
 * Home Screen
 * Main screen dengan semua sections
 *
 * File: ui/home/HomeScreen.kt
 *
 * FIXED:
 * - Height grid disesuaikan (bisa di-adjust lagi sesuai tinggi card actual)
 * - Tombol "Lebih Sedikit" untuk collapse kembali
 * - Auto scroll ke section Top Anime saat toggle
 * - Urutan konsisten dengan sorting di repository
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToTop: () -> Unit,
    onNavigateToSeasonal: (Int, String) -> Unit,
    onNavigateToUpcoming: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("IME Anime") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ==================== Hero Section ====================
            item {
                when (val heroState = state.heroAnime) {
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            LoadingIndicator()
                        }
                    }
                    is Resource.Success -> {
                        HeroSection(
                            anime = heroState.data,
                            onClick = { heroState.data?.let { onNavigateToDetail(it.id) } }
                        )
                    }
                    is Resource.Error -> {
                        ErrorStateSmall(message = heroState.message ?: "Failed to load hero")
                    }
                }
            }

            // ==================== Search Bar ====================
            item {
                SearchBarHome(
                    onClick = onNavigateToSearch,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // ==================== Section: Top 10 Anime ====================
            item {
                SectionHeader(
                    title = "Top 10 Anime",
                    onSeeAllClick = onNavigateToTop
                )
            }

            item {
                when (val topState = state.topAnime) {
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            LoadingIndicator()
                        }
                    }
                    is Resource.Success -> {
                        Column {
                            // Grid 2 kolom
                            // Height calculation (sesuaikan dengan tinggi card Anda):
                            // Jika card height ≈ 280dp:
                            //   4 items (2 rows) = 280 * 2 + spacing(12*1) = ~572dp
                            //   10 items (5 rows) = 280 * 5 + spacing(12*4) = ~1448dp
                            // Sesuaikan angka di bawah jika card Anda lebih tinggi/rendah
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(
                                        if (state.isTopExpanded) 1455.dp else 580.dp
                                    )
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                userScrollEnabled = false
                            ) {
                                items(state.getDisplayedTopAnime()) { anime ->
                                    AnimeCard(
                                        anime = anime,
                                        showRank = true,
                                        onClick = { onNavigateToDetail(anime.id) }
                                    )
                                }
                            }

                            // Tombol toggle "Lebih Banyak" / "Lebih Sedikit"
                            if (topState.data != null && topState.data.size > 4) {
                                Button(
                                    onClick = {
                                        viewModel.toggleTopExpanded()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 2.dp)
                                ) {
                                    Text(if (state.isTopExpanded) "Lebih Sedikit" else "Lebih Banyak")
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        ErrorStateSmall(
                            message = topState.message ?: "Failed to load top anime"
                        )
                    }
                }
            }

            // ==================== Section: Anime Musim Ini ====================
            item {
                SectionHeader(
                    title = "Anime Musim Ini",
                    onSeeAllClick = {
                        onNavigateToSeasonal(state.currentYear, state.currentSeason)
                    }
                )
            }

            item {
                when (val seasonState = state.currentSeasonAnime) {
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                        ) {
                            LoadingIndicator()
                        }
                    }
                    is Resource.Success -> {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(seasonState.data ?: emptyList()) { anime ->
                                AnimeCardHorizontal(
                                    anime = anime,
                                    onClick = { onNavigateToDetail(anime.id) }
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        ErrorStateSmall(
                            message = seasonState.message ?: "Failed to load seasonal anime"
                        )
                    }
                }
            }

            // ==================== Section: Anime Upcoming ====================
            item {
                SectionHeader(
                    title = "Anime Upcoming",
                    onSeeAllClick = onNavigateToUpcoming
                )
            }

            item {
                when (val upcomingState = state.upcomingAnime) {
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                        ) {
                            LoadingIndicator()
                        }
                    }
                    is Resource.Success -> {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(upcomingState.data ?: emptyList()) { anime ->
                                AnimeCardHorizontal(
                                    anime = anime,
                                    onClick = { onNavigateToDetail(anime.id) }
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        ErrorStateSmall(
                            message = upcomingState.message ?: "Failed to load upcoming anime"
                        )
                    }
                }
            }

            // ==================== Section: Anime Musim Lalu ====================
            item {
                SectionHeader(
                    title = "Anime Musim Lalu",
                    onSeeAllClick = {
                        onNavigateToSeasonal(state.previousYear, state.previousSeason)
                    }
                )
            }

            item {
                when (val prevSeasonState = state.previousSeasonAnime) {
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                        ) {
                            LoadingIndicator()
                        }
                    }
                    is Resource.Success -> {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(prevSeasonState.data ?: emptyList()) { anime ->
                                AnimeCardHorizontal(
                                    anime = anime,
                                    onClick = { onNavigateToDetail(anime.id) }
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        ErrorStateSmall(
                            message = prevSeasonState.message ?: "Failed to load previous season"
                        )
                    }
                }
            }
        }
    }
}