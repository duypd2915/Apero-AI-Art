package com.duyhellowolrd.ai_art_service.data

import com.duyhellowolrd.ai_art_service.data.params.AiArtParams
import com.duyhellowolrd.ai_art_service.network.response.StyleData

interface AiArtRepository {
    suspend fun genArtAi(params: AiArtParams): Result<String>

    suspend fun getAllStyles(): Result<StyleData>
}