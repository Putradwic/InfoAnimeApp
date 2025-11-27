package com.putradwicahyono.ime.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Images DTO - struktur nested untuk gambar anime
 * Jikan API menyediakan format JPG dan WebP
 * File: data/remote/dto/ImageDto.kt
 */
data class ImagesDto(
    @SerializedName("jpg")
    val jpg: ImageUrlDto,

    @SerializedName("webp")
    val webp: ImageUrlDto
)

/**
 * Image URL DTO - URL gambar dalam berbagai ukuran
 * - image_url: Normal quality (biasa untuk card)
 * - small_image_url: Small quality (thumbnail)
 * - large_image_url: High quality (untuk hero section/detail)
 */
data class ImageUrlDto(
    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("small_image_url")
    val smallImageUrl: String?,

    @SerializedName("large_image_url")
    val largeImageUrl: String?
)
