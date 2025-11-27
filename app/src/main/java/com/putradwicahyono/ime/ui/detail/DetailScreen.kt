package com.putradwicahyono.ime.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.putradwicahyono.ime.domain.model.Anime
import com.putradwicahyono.ime.ui.components.*
import com.putradwicahyono.ime.ui.theme.*

/**
 * Detail Screen
 * Menampilkan detail lengkap anime
 *
 * File: ui/detail/DetailScreen.kt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.getAnime()?.title ?: "Detail",
                        maxLines = 1
                    )
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Loading
                state.isLoading -> {
                    LoadingIndicator(message = "Loading anime detail...")
                }

                // Error
                state.error != null && !state.hasData() -> {
                    ErrorState(
                        message = state.error ?: "Failed to load anime detail",
                        onRetry = { viewModel.retry() }
                    )
                }

                // Success
                state.hasData() -> {
                    val anime = state.getAnime()!!
                    DetailContent(
                        anime = anime,
                        scrollState = scrollState
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailContent(
    anime: Anime,
    scrollState: androidx.compose.foundation.ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Header Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            // Background Image
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            ),
                            startY = 200f
                        )
                    )
            )
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = anime.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // English Title
            if (!anime.titleEnglish.isNullOrBlank() && anime.titleEnglish != anime.title) {
                Text(
                    text = anime.titleEnglish,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Japanese Title
            if (!anime.titleJapanese.isNullOrBlank()) {
                Text(
                    text = anime.titleJapanese,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Info Row (Type, Episodes, Status)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Type Badge
                InfoBadge(
                    text = anime.type,
                    backgroundColor = getTypeColor(anime.type)
                )

                // Episodes
                anime.episodes?.let { eps ->
                    InfoBadge(
                        text = "$eps eps",
                        backgroundColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                }

                // Status
                anime.status?.let { status ->
                    InfoBadge(
                        text = status,
                        backgroundColor = getStatusColor(status)
                    )
                }
            }

            // Score & Rank Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Score
                if (anime.score != null) {
                    InfoCard(
                        title = "Score",
                        value = anime.getFormattedScore(),
                        icon = "⭐",
                        color = getScoreColor(anime.score),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Rank
                anime.rank?.let { rank ->
                    InfoCard(
                        title = "Rank",
                        value = "#$rank",
                        icon = "🏆",
                        color = getRankColor(rank),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Popularity
                anime.popularity?.let { pop ->
                    InfoCard(
                        title = "Popularity",
                        value = "#$pop",
                        icon = "📊",
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            HorizontalDivider()

            // Season & Year
            anime.getSeasonYear()?.let { seasonYear ->
                InfoRow(
                    label = "Season",
                    value = seasonYear
                )
            }

            // Duration
            anime.duration?.let { duration ->
                InfoRow(
                    label = "Duration",
                    value = duration
                )
            }

            // Rating
            anime.rating?.let { rating ->
                InfoRow(
                    label = "Rating",
                    value = rating
                )
            }

            HorizontalDivider()

            // Genres
            if (anime.genres.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Genres",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        anime.genres.take(5).forEach { genre ->
                            AssistChip(
                                onClick = { },
                                label = { Text(genre) }
                            )
                        }
                    }
                }
            }

            // Studios
            if (anime.studios.isNotEmpty()) {
                InfoRow(
                    label = "Studios",
                    value = anime.studios.joinToString(", ")
                )
            }

            HorizontalDivider()

            // Synopsis
            if (!anime.synopsis.isNullOrBlank()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Synopsis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = anime.synopsis,
                        style = SynopsisStyle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Bottom spacing
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InfoBadge(
    text: String,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = BadgeStyle,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    value: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

