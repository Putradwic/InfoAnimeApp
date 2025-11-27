package com.putradwicahyono.ime.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.putradwicahyono.ime.data.repository.AnimeRepository
import com.putradwicahyono.ime.domain.model.Anime
import com.putradwicahyono.ime.util.Constants
import com.putradwicahyono.ime.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Search ViewModel
 * Handle search logic dengan debounce
 *
 * File: ui/search/SearchViewModel.kt
 */
class SearchViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var searchJob: Job? = null
    private val allResults = mutableListOf<Anime>()

    /**
     * Update search query dengan debounce
     */
    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }

        // Cancel previous search
        searchJob?.cancel()

        // Reset jika query kosong
        if (query.trim().isEmpty()) {
            _state.update {
                it.copy(
                    searchResults = Resource.Success(emptyList()),
                    currentPage = 1,
                    hasNextPage = false
                )
            }
            allResults.clear()
            return
        }

        // Debounce search (tunggu 500ms)
        searchJob = viewModelScope.launch {
            delay(Constants.SEARCH_DEBOUNCE_DELAY)
            if (query.trim().length >= 2) {
                searchAnime(query.trim())
            }
        }
    }

    /**
     * Search anime
     */
    private fun searchAnime(query: String, page: Int = 1) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSearching = true,
                    error = null
                )
            }

            repository.searchAnime(
                query = query,
                page = page,
                limit = 10
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isSearching = true) }
                    }
                    is Resource.Success -> {
                        val newResults = result.data ?: emptyList()

                        // Jika page 1, replace. Jika page > 1, append
                        if (page == 1) {
                            allResults.clear()
                            allResults.addAll(newResults)
                        } else {
                            allResults.addAll(newResults)
                        }

                        _state.update {
                            it.copy(
                                searchResults = Resource.Success(allResults.toList()),
                                currentPage = page,
                                hasNextPage = newResults.size >= 10,
                                isSearching = false,
                                isLoadingMore = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                searchResults = if (page == 1) {
                                    Resource.Error(result.message ?: "Search failed")
                                } else {
                                    // Keep existing results jika load more gagal
                                    Resource.Success(allResults.toList())
                                },
                                isSearching = false,
                                isLoadingMore = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Load more results (pagination)
     */
    fun loadMore() {
        val currentState = _state.value

        if (currentState.isLoadingMore ||
            !currentState.hasNextPage ||
            currentState.query.trim().isEmpty()) {
            return
        }

        _state.update { it.copy(isLoadingMore = true) }
        searchAnime(currentState.query.trim(), currentState.currentPage + 1)
    }

    /**
     * Clear search
     */
    fun clearSearch() {
        searchJob?.cancel()
        allResults.clear()
        _state.update {
            SearchState() // Reset ke initial state
        }
    }

    /**
     * Retry search
     */
    fun retry() {
        val query = _state.value.query.trim()
        if (query.length >= 2) {
            searchAnime(query)
        }
    }
}

/**
 * ViewModel Factory
 */
class SearchViewModelFactory(
    private val repository: AnimeRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
