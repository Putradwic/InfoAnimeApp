package com.putradwicahyono.ime.ui.seasonal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.putradwicahyono.ime.data.repository.AnimeRepository
import com.putradwicahyono.ime.domain.model.Anime
import com.putradwicahyono.ime.util.Constants
import com.putradwicahyono.ime.util.Resource
import com.putradwicahyono.ime.util.SeasonUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Seasonal ViewModel
 * Handle filter year & season + pagination
 *
 * File: ui/seasonal/SeasonalViewModel.kt
 */
class SeasonalViewModel(
    private val repository: AnimeRepository,
    initialYear: Int,
    initialSeason: String
) : ViewModel() {

    private val _state = MutableStateFlow(
        SeasonalState(
            selectedYear = initialYear,
            selectedSeason = initialSeason,
            availableYears = generateAvailableYears()
        )
    )
    val state: StateFlow<SeasonalState> = _state.asStateFlow()

    private val allAnime = mutableListOf<Anime>()

    init {
        loadSeasonalAnime()
    }

    /**
     * Generate available years (2000 - current year + 1)
     */
    private fun generateAvailableYears(): List<Int> {
        val currentYear = LocalDate.now().year
        return (2000..currentYear + 1).toList().reversed()
    }

    /**
     * Load seasonal anime
     */
    private fun loadSeasonalAnime(page: Int = 1) {
        // Prevent multiple simultaneous loads
        if (_state.value.isLoading || _state.value.isLoadingMore) {
            return
        }

        viewModelScope.launch {
            val currentState = _state.value

            // Update loading state
            _state.update {
                it.copy(
                    isLoading = page == 1,
                    isLoadingMore = page > 1,
                    error = null
                )
            }

            repository.getSeasonalAnime(
                year = currentState.selectedYear,
                season = currentState.selectedSeason,
                page = page,
                limit = 10
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Already handled above
                    }
                    is Resource.Success -> {
                        val newAnime = result.data ?: emptyList()

                        // If page 1, replace. If page > 1, append
                        if (page == 1) {
                            allAnime.clear()
                            allAnime.addAll(newAnime)
                        } else {
                            allAnime.addAll(newAnime)
                        }

                        _state.update {
                            it.copy(
                                seasonalAnime = allAnime.toList(),
                                currentPage = page,
                                hasNextPage = newAnime.size >= 10,
                                isLoading = false,
                                isLoadingMore = false,
                                isInitialLoad = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                error = result.message,
                                // Keep previous data if load more failed
                                seasonalAnime = if (page > 1) allAnime.toList() else emptyList(),
                                isInitialLoad = page == 1
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Change year filter
     */
    fun onYearSelected(year: Int) {
        if (year == _state.value.selectedYear) return

        _state.update {
            it.copy(
                selectedYear = year,
                currentPage = 1,
                hasNextPage = true,
                isInitialLoad = true
            )
        }

        allAnime.clear()
        loadSeasonalAnime(page = 1)
    }

    /**
     * Change season filter
     */
    fun onSeasonSelected(season: String) {
        if (season == _state.value.selectedSeason) return

        _state.update {
            it.copy(
                selectedSeason = season,
                currentPage = 1,
                hasNextPage = true,
                isInitialLoad = true
            )
        }

        allAnime.clear()
        loadSeasonalAnime(page = 1)
    }

    /**
     * Load more (next page)
     */
    fun loadMore() {
        val currentState = _state.value

        if (currentState.canLoadMore()) {
            loadSeasonalAnime(currentState.currentPage + 1)
        }
    }

    /**
     * Retry loading
     */
    fun retry() {
        if (_state.value.seasonalAnime.isEmpty()) {
            loadSeasonalAnime(page = 1)
        } else {
            loadMore()
        }
    }

    /**
     * Refresh (reload from page 1)
     */
    fun refresh() {
        allAnime.clear()
        _state.update {
            it.copy(
                currentPage = 1,
                hasNextPage = true,
                isInitialLoad = true
            )
        }
        loadSeasonalAnime(page = 1)
    }
}

/**
 * ViewModel Factory
 */
class SeasonalViewModelFactory(
    private val repository: AnimeRepository,
    private val initialYear: Int,
    private val initialSeason: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SeasonalViewModel::class.java)) {
            return SeasonalViewModel(repository, initialYear, initialSeason) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
