package com.putradwicahyono.ime.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Response wrapper untuk single anime
 * Digunakan untuk: Random Anime, Anime Detail
 * File: data/remote/dto/AnimeListResponse.kt
 */
data class AnimeResponse(
    @SerializedName("data")
    val data: AnimeDto
)

/**
 * Response wrapper untuk list anime dengan pagination
 * Digunakan untuk: Top Anime, Seasonal, Upcoming, Search
 * File: data/remote/dto/AnimeListResponse.kt
 */
data class AnimeListResponse(
    @SerializedName("data")
    val data: List<AnimeDto>,

    @SerializedName("pagination")
    val pagination: PaginationDto
)

/**
 * Pagination DTO - informasi pagination dari API
 */
data class PaginationDto(
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int,

    @SerializedName("has_next_page")
    val hasNextPage: Boolean,

    @SerializedName("current_page")
    val currentPage: Int,

    @SerializedName("items")
    val items: PaginationItemsDto
)

/**
 * Pagination Items DTO - detail item count per page
 */
data class PaginationItemsDto(
    @SerializedName("count")
    val count: Int,

    @SerializedName("total")
    val total: Int,

    @SerializedName("per_page")
    val perPage: Int
)
