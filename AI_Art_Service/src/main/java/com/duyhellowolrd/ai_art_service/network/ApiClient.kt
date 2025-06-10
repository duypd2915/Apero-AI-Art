package com.duyhellowolrd.ai_art_service.network

import com.duyhellowolrd.ai_art_service.AiArtServiceEntry
import com.duyhellowolrd.ai_art_service.network.interceptors.SignatureInterceptor
import com.duyhellowolrd.ai_art_service.network.interceptors.createLoggingInterceptor
import com.duyhellowolrd.ai_art_service.network.service.AIStyleService
import com.duyhellowolrd.ai_art_service.network.service.AiArtService
import com.duyhellowolrd.ai_art_service.network.service.TimeStampService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val REQUEST_TIMEOUT: Long = 30

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(SignatureInterceptor())
            .addInterceptor(createLoggingInterceptor())
            .build()
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun getTimeStampService(): TimeStampService =
        buildRetrofit(AiArtServiceEntry.ART_SERVICE_URL).create()

    fun getStyleService(): AIStyleService = buildRetrofit(AiArtServiceEntry.ART_STYLE_URL).create()
    fun getAiArtService(): AiArtService {
        return buildRetrofit(AiArtServiceEntry.ART_SERVICE_URL).create(AiArtService::class.java)
    }
}