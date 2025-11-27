package com.putradwicahyono.ime.ui.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.putradwicahyono.ime.data.repository.AnimeRepository
import com.putradwicahyono.ime.domain.model.Anime
import com.putradwicahyono.ime.util.Resource
import com.putradwicahyono.ime.util.SeasonUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Upcoming ViewModel
 * Handle upcoming anime dengan filter by year (tabs)
 * Load semua data dari API, filter di client side by year
 *
 * File: ui/upcoming/UpcomingViewModel.kt
 */
class UpcomingViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        UpcomingState(
            availableYears = SeasonUtil.getUpcomingYears(3), // Current + 2 years
            selectedYear = SeasonUtil.getCurrentYear()
        )
    )
    val state: StateFlow<UpcomingState> = _state.asStateFlow()

    private val allAnime = mutableListOf<Anime>()

    init {
        loadUpcomingAnime()
    }

    /**
     * Load upcoming anime dari API
     * API tidak support filter by year, jadi fetch semua lalu filter client-side
     */
    private fun loadUpcomingAnime(page: Int = 1) {
        // Prevent multiple simultaneous loads
        if (_state.value.isLoading || _state.value.isLoadingMore) {
            return
        }

        viewModelScope.launch {
            // Update loading state
            _state.update {
                it.copy(
                    isLoading = page == 1,
                    isLoadingMore = page > 1,
                    error = null
                )
            }

            repository.getUpcomingAnime(
                page = page,
                limit = 25 // Load more per page karena nanti di-filter
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

                        // Filter by selected year
                        val filteredAnime = filterAnimeByYear(
                            allAnime.toList(),
                            _state.value.selectedYear
                        )

                        _state.update {
                            it.copy(
                                allUpcomingAnime = allAnime.toList(),
                                displayedAnime = filteredAnime,
                                currentPage = page,
                                hasNextPage = newAnime.size >= 25,
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
                                isInitialLoad = page == 1
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Filter anime by year
     */
    private fun filterAnimeByYear(animeList: List<Anime>, year: Int): List<Anime> {
        return animeList.filter { anime ->
            anime.year == year
        }
    }

    /**
     * Change year tab
     */
    fun onYearSelected(year: Int) {
        if (year == _state.value.selectedYear) return

        // Filter existing data
        val filteredAnime = filterAnimeByYear(
            _state.value.allUpcomingAnime,
            year
        )

        _state.update {
            it.copy(
                selectedYear = year,
                displayedAnime = filteredAnime
            )
        }
    }

    /**
     * Load more (next page)
     */
    fun loadMore() {
        val currentState = _state.value

        if (currentState.canLoadMore()) {
            loadUpcomingAnime(currentState.currentPage + 1)
        }
    }

    /**
     * Retry loading
     */
    fun retry() {
        if (_state.value.allUpcomingAnime.isEmpty()) {
            loadUpcomingAnime(page = 1)
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
                allUpcomingAnime = emptyList(),
                displayedAnime = emptyList(),
                currentPage = 1,
                hasNextPage = true,
                isInitialLoad = true
            )
        }
        loadUpcomingAnime(page = 1)
    }
}

/**
 * ViewModel Factory
 */
class UpcomingViewModelFactory(
    private val repository: AnimeRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
            return UpcomingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}