package com.putradwicahyono.ime.data.remote

import com.putradwicahyono.ime.data.remote.dto.AnimeListResponse
import com.putradwicahyono.ime.data.remote.dto.AnimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Jikan API Service Interface
 * Base URL: https://api.jikan.moe/v4/
 * Documentation: https://docs.api.jikan.moe/
 *
 * File: data/remote/JikanApiService.kt
 */
interface JikanApiService {

    /**
     * Get Random Anime
     * Endpoint: GET /random/anime
     * Untuk: Hero Section di Home Screen
     *
     * Response: Single anime random
     */
    @GET("random/anime")
    suspend fun getRandomAnime(): Response<AnimeResponse>

    /**
     * Get Top Anime
     * Endpoint: GET /top/anime
     * Untuk: Section "Top 10 Anime" & Top Screen
     *
     * @param page Halaman (default: 1)
     * @param limit Jumlah item per halaman (default: 10, max: 25)
     *
     * Response: List anime dengan ranking tertinggi
     */
    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 10
    ): Response<AnimeListResponse>


    /**
     * Get Seasonal Anime
     * Endpoint: GET /seasons/{year}/{season}
     * Untuk: Section "Anime Musim Ini" & "Anime Musim Lalu" & Seasonal Screen
     *
     * @param year Tahun (contoh: 2025)
     * @param season Musim: "winter", "spring", "summer", "fall"
     * @param page Halaman untuk pagination
     * @param limit Jumlah item per halaman
     *
     * Response: List anime yang tayang di musim tertentu
     */
    @GET("seasons/{year}/{season}")
    suspend fun getSeasonalAnime(
        @Path("year") year: Int,
        @Path("season") season: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<AnimeListResponse>

    /**
     * Get Current Season Anime
     * Endpoint: GET /seasons/now
     * Alternative untuk musim saat ini (auto-detect)
     *
     * @param page Halaman
     * @param limit Jumlah item per halaman
     *
     * Response: List anime musim saat ini (auto-detect oleh API)
     */
    @GET("seasons/now")
    suspend fun getCurrentSeasonAnime(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<AnimeListResponse>

    /**
     * Get Upcoming Anime
     * Endpoint: GET /seasons/upcoming
     * Untuk: Section "Anime Upcoming" & Upcoming Screen
     *
     * @param page Halaman
     * @param limit Jumlah item per halaman
     *
     * Response: List anime yang akan tayang (upcoming)
     * Note: API tidak filter by year, jadi filter di client side
     */
    @GET("seasons/upcoming")
    suspend fun getUpcomingAnime(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25
    ): Response<AnimeListResponse>

    /**
     * Search Anime
     * Endpoint: GET /anime
     * Untuk: Search Screen
     *
     * @param query Keyword pencarian
     * @param page Halaman
     * @param limit Jumlah item per halaman
     * @param orderBy Sorting: "title", "score", "popularity", "favorites", "start_date"
     * @param sort Ascending ("asc") atau Descending ("desc")
     *
     * Response: List anime hasil pencarian
     */
    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("order_by") orderBy: String? = null,
        @Query("sort") sort: String? = null
    ): Response<AnimeListResponse>

    /**
     * Get Anime by ID
     * Endpoint: GET /anime/{id}
     * Untuk: Detail Screen (future feature)
     *
     * @param id MAL ID anime
     *
     * Response: Detail lengkap satu anime
     */
    @GET("anime/{id}")
    suspend fun getAnimeById(
        @Path("id") id: Int
    ): Response<AnimeResponse>

    /**
     * Get Anime by ID with Full data
     * Endpoint: GET /anime/{id}/full
     * Alternative untuk detail yang lebih lengkap (relations, characters, etc)
     *
     * @param id MAL ID anime
     *
     * Response: Detail super lengkap dengan relasi
     */
    @GET("anime/{id}/full")
    suspend fun getAnimeByIdFull(
        @Path("id") id: Int
    ): Response<AnimeResponse>

    /**
     * Get Anime Recommendations
     * Endpoint: GET /anime/{id}/recommendations
     * Untuk: Future feature - anime yang mirip/recommended
     *
     * @param id MAL ID anime
     */
    @GET("anime/{id}/recommendations")
    suspend fun getAnimeRecommendations(
        @Path("id") id: Int
    ): Response<AnimeListResponse>
}

/**
 * Helper object untuk Season mapping
 */
object SeasonConstants {
    const val WINTER = "winter"   // Dec, Jan, Feb
    const val SPRING = "spring"   // Mar, Apr, May
    const val SUMMER = "summer"   // Jun, Jul, Aug
    const val FALL = "fall"       // Sep, Oct, Nov

    /**
     * Get all seasons
     */
    fun getAllSeasons() = listOf(WINTER, SPRING, SUMMER, FALL)
}

/**
 * Helper object untuk sorting options
 */
object SortConstants {
    const val TITLE = "title"
    const val SCORE = "score"
    const val POPULARITY = "popularity"
    const val FAVORITES = "favorites"
    const val START_DATE = "start_date"

    const val ASC = "asc"
    const val DESC = "desc"
}
