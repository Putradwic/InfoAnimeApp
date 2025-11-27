package com.putradwicahyono.ime.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.putradwicahyono.ime.data.repository.AnimeRepository
import com.putradwicahyono.ime.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Detail ViewModel
 * Handle anime detail data
 *
 * File: ui/detail/DetailViewModel.kt
 */
class DetailViewModel(
    private val repository: AnimeRepository,
    private val animeId: Int
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    init {
        loadAnimeDetail()
    }

    /**
     * Load anime detail by ID
     */
    private fun loadAnimeDetail() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.getAnimeById(animeId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                anime = result,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                anime = result,
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Retry loading
     */
    fun retry() {
        loadAnimeDetail()
    }
}

/**
 * ViewModel Factory
 */
class DetailViewModelFactory(
    private val repository: AnimeRepository,
    private val animeId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository, animeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
