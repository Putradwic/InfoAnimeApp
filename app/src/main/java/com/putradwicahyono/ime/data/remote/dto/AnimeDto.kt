package com.putradwicahyono.ime.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Main Anime DTO - sesuai dengan Jikan API v4 response
 * File: data/remote/dto/AnimeDto.kt
 */
data class AnimeDto(
    @SerializedName("mal_id")
    val malId: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("title_english")
    val titleEnglish: String?,

    @SerializedName("title_japanese")
    val titleJapanese: String?,

    @SerializedName("images")
    val images: ImagesDto,

    @SerializedName("type")
    val type: String?, // TV, Movie, OVA, Special, ONA, Music

    @SerializedName("episodes")
    val episodes: Int?,

    @SerializedName("status")
    val status: String?, // Finished Airing, Currently Airing, Not yet aired

    @SerializedName("score")
    val score: Double?,

    @SerializedName("scored_by")
    val scoredBy: Int?,

    @SerializedName("rank")
    val rank: Int?,

    @SerializedName("popularity")
    val popularity: Int?,

    @SerializedName("members")
    val members: Int?,

    @SerializedName("favorites")
    val favorites: Int?,

    @SerializedName("synopsis")
    val synopsis: String?,

    @SerializedName("year")
    val year: Int?,

    @SerializedName("season")
    val season: String?, // winter, spring, summer, fall

    @SerializedName("rating")
    val rating: String?, // G, PG, PG-13, R, R+, Rx

    @SerializedName("duration")
    val duration: String?,

    @SerializedName("genres")
    val genres: List<GenreDto>?,

    @SerializedName("studios")
    val studios: List<StudioDto>?,

    @SerializedName("aired")
    val aired: AiredDto?
)

/**
 * Genre DTO
 */
data class GenreDto(
    @SerializedName("mal_id")
    val malId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("type")
    val type: String
)

/**
 * Studio DTO
 */
data class StudioDto(
    @SerializedName("mal_id")
    val malId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("type")
    val type: String
)

/**
 * Aired DTO - informasi tanggal tayang
 */
data class AiredDto(
    @SerializedName("from")
    val from: String?,

    @SerializedName("to")
    val to: String?,

    @SerializedName("string")
    val string: String?
)
