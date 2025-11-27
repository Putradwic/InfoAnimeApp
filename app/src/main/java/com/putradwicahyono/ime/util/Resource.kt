package com.putradwicahyono.ime.util

/**
 * Resource Sealed Class
 * Untuk handle state management (Loading, Success, Error)
 *
 * File: util/Resource.kt
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Loading State
     * Digunakan saat fetch data dari API
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)

    /**
     * Success State
     * Digunakan saat data berhasil di-fetch
     * @param data Data yang berhasil di-fetch
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Error State
     * Digunakan saat terjadi error
     * @param message Pesan error
     * @param data Data optional (untuk keep previous data)
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}

/**
 * Extension function untuk check state
 */
fun <T> Resource<T>.isLoading(): Boolean = this is Resource.Loading
fun <T> Resource<T>.isSuccess(): Boolean = this is Resource.Success
fun <T> Resource<T>.isError(): Boolean = this is Resource.Error