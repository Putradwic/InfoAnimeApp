package com.putradwicahyono.ime.di

import com.putradwicahyono.ime.data.remote.JikanApiService
import com.putradwicahyono.ime.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Network Module - Manual Dependency Injection
 * Provide Retrofit, OkHttp, dan ApiService
 *
 * File: di/NetworkModule.kt
 */
object NetworkModule {

    // Singleton OkHttpClient
    @Volatile
    private var okHttpClient: OkHttpClient? = null

    /**
     * Provide OkHttpClient dengan logging interceptor
     * - Read timeout: 30 detik
     * - Connect timeout: 30 detik
     * - Logging level: BODY (hanya di debug mode)
     */
    fun provideOkHttpClient(): OkHttpClient {
        return okHttpClient ?: synchronized(this) {
            okHttpClient ?: OkHttpClient.Builder().apply {
                // Timeout configuration
                readTimeout(30, TimeUnit.SECONDS)
                connectTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)

                // Logging Interceptor (hanya untuk debug)
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = if (com.putradwicahyono.ime.BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
                addInterceptor(loggingInterceptor)

                // Retry on connection failure
                retryOnConnectionFailure(true)

            }.build().also {
                okHttpClient = it
            }
        }
    }

    // Singleton Retrofit
    @Volatile
    private var retrofit: Retrofit? = null

    /**
     * Provide Retrofit instance
     * Base URL: https://api.jikan.moe/v4/
     */
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return retrofit ?: synchronized(this) {
            retrofit ?: Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .also {
                    retrofit = it
                }
        }
    }

    // Singleton JikanApiService
    @Volatile
    private var jikanApiService: JikanApiService? = null

    /**
     * Provide JikanApiService
     * Depends on: Retrofit
     */
    fun provideJikanApiService(retrofit: Retrofit): JikanApiService {
        return jikanApiService ?: synchronized(this) {
            jikanApiService ?: retrofit.create(JikanApiService::class.java).also {
                jikanApiService = it
            }
        }
    }

    /**
     * Shortcut: Get ApiService langsung tanpa manual provide
     * Otomatis provide OkHttp → Retrofit → ApiService
     */
    fun getApiService(): JikanApiService {
        val client = provideOkHttpClient()
        val retrofit = provideRetrofit(client)
        return provideJikanApiService(retrofit)
    }

    /**
     * Reset semua network dependencies (untuk testing)
     */
    fun reset() {
        okHttpClient = null
        retrofit = null
        jikanApiService = null
    }
}
