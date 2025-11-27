package com.putradwicahyono.ime.data.repository

import com.putradwicahyono.ime.data.remote.JikanApiService
import com.putradwicahyono.ime.data.remote.dto.AnimeDto
import com.putradwicahyono.ime.domain.model.Anime
import com.putradwicahyono.ime.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Anime Repository
 * Handle semua API calls dan convert DTO ke Domain Model
 *
 * File: data/repository/AnimeRepository.kt
 */
class AnimeRepository(
    private val api: JikanApiService
) {

    /**
     * Get Random Anime untuk Hero Section
     * Returns: Flow<Resource<Anime>>
     */
    fun getRandomAnime(): Flow<Resource<Anime>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getRandomAnime()

            if (response.isSuccessful && response.body() != null) {
                val animeDto = response.body()!!.data
                val anime = animeDto.toDomainModel()
                emit(Resource.Success(anime))
            } else {
                emit(Resource.Error("Failed to fetch random anime"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Connection error. Please check your internet."))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }

    /**
     * Get Top Anime dengan pagination
     * @param page Halaman (default: 1)
     * @param limit Jumlah item (default: 10)
     * Returns: Flow<Resource<List<Anime>>>
     *
     * FIXED: Tambah sorting berdasarkan rank untuk konsistensi urutan
     */
    fun getTopAnime(
        page: Int = 1,
        limit: Int = 10
    ): Flow<Resource<List<Anime>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getTopAnime(page = page, limit = limit)

            if (response.isSuccessful && response.body() != null) {
                val animeList = response.body()!!.data
                    .map { it.toDomainModel() }
                    .sortedBy { it.rank } // ← FIXED: Sort by rank ascending (1, 2, 3, ...)

                emit(Resource.Success(animeList))
            } else {
                emit(Resource.Error("Failed to fetch top anime"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Connection error. Please check your internet."))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }

    /**
     * Get Seasonal Anime
     * @param year Tahun (contoh: 2025)
     * @param season Musim: winter, spring, summer, fall
     * @param page Halaman
     * @param limit Jumlah item
     * Returns: Flow<Resource<List<Anime>>>
     */
    fun getSeasonalAnime(
        year: Int,
        season: String,
        page: Int = 1,
        limit: Int = 10
    ): Flow<Resource<List<Anime>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getSeasonalAnime(
                year = year,
                season = season,
                page = page,
                limit = limit
            )

            if (response.isSuccessful && response.body() != null) {
                val animeList = response.body()!!.data.map { it.toDomainModel() }
                emit(Resource.Success(animeList))
            } else {
                emit(Resource.Error("Failed to fetch seasonal anime"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Connection error. Please check your internet."))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }

    /**
     * Get Current Season Anime (auto-detect by API)
     * Alternative untuk musim saat ini
     */
    fun getCurrentSeasonAnime(
        page: Int = 1,
        limit: Int = 10
    ): Flow<Resource<List<Anime>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getCurrentSeasonAnime(page = page, limit = limit)

            if (response.isSuccessful && response.body() != null) {
                val animeList = response.body()!!.data.map { it.toDomainModel() }
                emit(Resource.Success(animeList))
            } else {
                emit(Resource.Error("Failed to fetch current season anime"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Connection error. Please check your internet."))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }

    /**
     * Get Upcoming Anime
     * @param page Halaman
     * @param limit Jumlah item
     * Returns: Flow<Resource<List<Anime>>>
     */
    fun getUpcomingAnime(
        page: Int = 1,
        limit: Int = 25
    ): Flow<Resource<List<Anime>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getUpcomingAnime(page = page, limit = limit)

            if (response.isSuccessful && response.body() != null) {
                val animeList = response.body()!!.data.map { it.toDomainModel() }
                emit(Resource.Success(animeList))
            } else {
                emit(Resource.Error("Failed to fetch upcoming anime"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Connection error. Please check your internet."))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }

    /**
     * Search Anime
     * @param query Keyword pencarian
     * @param page Halaman
     * @param limit Jumlah item
     * Returns: Flow<Resource<List<Anime>>>
     */
    fun searchAnime(
        query: String,
        page: Int = 1,
        limit: Int = 10
    ): Flow<Resource<List<Anime>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.searchAnime(
                query = query,
                page = page,
                limit = limit
            )

            if (response.isSuccessful && response.body() != null) {
                val animeList = response.body()!!.data.map { it.toDomainModel() }
                emit(Resource.Success(animeList))
            } else {
                emit(Resource.Error("Failed to search anime"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Connection error. Please check your internet."))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }

    /**
     * Get Anime By ID (untuk detail screen - future feature)
     * @param id MAL ID anime
     * Returns: Flow<Resource<Anime>>
     */
    fun getAnimeById(id: Int): Flow<Resource<Anime>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getAnimeById(id)

            if (response.isSuccessful && response.body() != null) {
                val anime = response.body()!!.data.toDomainModel()
                emit(Resource.Success(anime))
            } else {
                emit(Resource.Error("Failed to fetch anime detail"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Connection error. Please check your internet."))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }

    /**
     * Extension function: Convert AnimeDto to Domain Model
     * Mapper dari DTO layer ke Domain layer
     */
    private fun AnimeDto.toDomainModel(): Anime {
        return Anime(
            id = this.malId,
            title = this.title,
            titleEnglish = this.titleEnglish,
            titleJapanese = this.titleJapanese,
            imageUrl = this.images.jpg.largeImageUrl
                ?: this.images.jpg.imageUrl
                ?: "",
            type = this.type ?: "Unknown",
            episodes = this.episodes,
            status = this.status,
            score = this.score,
            rank = this.rank,
            popularity = this.popularity,
            synopsis = this.synopsis,
            year = this.year,
            season = this.season,
            rating = this.rating,
            duration = this.duration,
            genres = this.genres?.map { it.name } ?: emptyList(),
            studios = this.studios?.map { it.name } ?: emptyList()
        )
    }
}
