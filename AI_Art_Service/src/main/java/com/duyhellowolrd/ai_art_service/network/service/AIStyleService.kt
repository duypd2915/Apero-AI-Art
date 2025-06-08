package com.duyhellowolrd.ai_art_service.network.service

import com.duyhellowolrd.ai_art_service.network.response.StyleResponse
import retrofit2.Response
import retrofit2.http.GET

interface AIStyleService {
    @GET("v2/styles?page=1&limit=10&project=Artimind")
    suspend fun getStyles(): Response<StyleResponse>
}