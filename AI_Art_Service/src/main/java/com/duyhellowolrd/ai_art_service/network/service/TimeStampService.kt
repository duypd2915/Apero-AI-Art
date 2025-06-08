package com.duyhellowolrd.ai_art_service.network.service

import com.duyhellowolrd.ai_art_service.network.response.TimeStampResponse
import retrofit2.Response
import retrofit2.http.GET

interface TimeStampService {
    @GET("/timestamp")
    suspend fun getTimestamp(): Response<TimeStampResponse>
}