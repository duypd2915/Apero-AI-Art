package com.duyhellowolrd.ai_art_service.network.service

import com.duyhellowolrd.ai_art_service.network.response.StyleResponse
import retrofit2.Response
import retrofit2.http.GET

interface AIStyleService {
    @GET("category?project=techtrek&segmentValue=IN&styleType=imageToImage&isApp=true")
    suspend fun getStyles(): Response<StyleResponse>
}