package com.putradwicahyono.ime.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.putradwicahyono.ime.data.repository.AnimeRepository
import com.putradwicahyono.ime.util.Resource
import com.putradwicahyono.ime.util.SeasonUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Home ViewModel
 * Handle business logic dan data untuk Home Screen
 *
 * File: ui/home/HomeViewModel.kt
 */
class HomeViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadAllData()
    }

    /**
     * Load semua data untuk home screen
     * FIXED: Tambah delay untuk avoid rate limiting (max 3 req/sec dari Jikan API)
     */
    fun loadAllData() {
        viewModelScope.launch {
            // Load hero anime dulu
            loadHeroAnime()

            delay(400) // Delay 400ms

            // Load top anime
            loadTopAnime()

            delay(400)

            // Load current season
            loadCurrentSeasonAnime()

            delay(400)

            // Load previous season
            loadPreviousSeasonAnime()

            delay(400)

            // Load upcoming
            loadUpcomingAnime()
        }
    }

    /**
     * Refresh semua data
     */
    fun refresh() {
        _state.update { it.copy(isRefreshing = true) }
        loadAllData()
    }

    /**
     * Load random anime untuk hero section
     */
    private fun loadHeroAnime() {
        viewModelScope.launch {
            repository.getRandomAnime().collect { result ->
                _state.update { it.copy(heroAnime = result) }
            }
        }
    }

    /**
     * Load top 10 anime
     * NOTE: Request 25 items untuk memastikan dapat minimal 10 setelah filtering
     */
    private fun loadTopAnime() {
        viewModelScope.launch {
            repository.getTopAnime(page = 1, limit = 25).collect { result ->
                _state.update { it.copy(topAnime = result) }
            }
        }
    }

    /**
     * Load anime musim saat ini (realtime)
     */
    private fun loadCurrentSeasonAnime() {
        viewModelScope.launch {
            val currentYear = SeasonUtil.getCurrentYear()
            val currentSeason = SeasonUtil.getCurrentSeason()

            repository.getSeasonalAnime(
                year = currentYear,
                season = currentSeason,
                page = 1,
                limit = 10
            ).collect { result ->
                _state.update {
                    it.copy(
                        currentSeasonAnime = result,
                        currentYear = currentYear,
                        currentSeason = currentSeason
                    )
                }
            }
        }
    }

    /**
     * Load anime musim sebelumnya (realtime)
     */
    private fun loadPreviousSeasonAnime() {
        viewModelScope.launch {
            val currentSeason = SeasonUtil.getCurrentSeason()
            val previousSeason = SeasonUtil.getPreviousSeason(currentSeason)
            val previousYear = SeasonUtil.getPreviousSeasonYear()

            repository.getSeasonalAnime(
                year = previousYear,
                season = previousSeason,
                page = 1,
                limit = 10
            ).collect { result ->
                _state.update {
                    it.copy(
                        previousSeasonAnime = result,
                        previousYear = previousYear,
                        previousSeason = previousSeason
                    )
                }
            }
        }
    }

    /**
     * Load upcoming anime
     */
    private fun loadUpcomingAnime() {
        viewModelScope.launch {
            repository.getUpcomingAnime(page = 1, limit = 10).collect { result ->
                _state.update { it.copy(upcomingAnime = result) }
            }
        }
    }

    /**
     * Toggle expand/collapse untuk section Top 10 Anime
     */
    fun toggleTopExpanded() {
        _state.update { it.copy(isTopExpanded = !it.isTopExpanded) }
    }

    /**
     * Retry saat error
     */
    fun retry() {
        loadAllData()
    }
}

/**
 * ViewModel Factory
 */
class HomeViewModelFactory(
    private val repository: AnimeRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}