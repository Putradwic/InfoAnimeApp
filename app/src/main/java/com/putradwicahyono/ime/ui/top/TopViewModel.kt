package com.putradwicahyono.ime.ui.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.putradwicahyono.ime.data.repository.AnimeRepository
import com.putradwicahyono.ime.domain.model.Anime
import com.putradwicahyono.ime.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Top ViewModel
 * Handle infinite scroll pagination untuk top anime
 *
 * File: ui/top/TopViewModel.kt
 */
class TopViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TopState())
    val state: StateFlow<TopState> = _state.asStateFlow()

    private val allAnime = mutableListOf<Anime>()

    init {
        loadTopAnime()
    }

    /**
     * Load top anime (initial or next page)
     */
    fun loadTopAnime(page: Int = 1) {
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

            repository.getTopAnime(
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
                                topAnime = allAnime.sortedBy { anime -> anime.rank }, // ← FIXED: Sort by rank
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
                                // Keep previous data if load more failed, also sorted
                                topAnime = if (page > 1) allAnime.sortedBy { anime -> anime.rank } else emptyList(),
                                isInitialLoad = page == 1
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Load more (next page)
     */
    fun loadMore() {
        val currentState = _state.value

        if (currentState.canLoadMore()) {
            loadTopAnime(currentState.currentPage + 1)
        }
    }

    /**
     * Retry loading
     */
    fun retry() {
        if (_state.value.topAnime.isEmpty()) {
            loadTopAnime(page = 1)
        } else {
            loadMore()
        }
    }

    /**
     * Refresh (reload from page 1)
     */
    fun refresh() {
        allAnime.clear()
        _state.update { TopState() }
        loadTopAnime(page = 1)
    }

    /**
     * Get displayed top anime with proper sorting
     * Alternative method if you want to sort on-demand
     */
    fun getDisplayedTopAnime(): List<Anime> {
        return _state.value.topAnime.sortedBy { it.rank }
    }
}

/**
 * ViewModel Factory
 */
class TopViewModelFactory(
    private val repository: AnimeRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopViewModel::class.java)) {
            return TopViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}