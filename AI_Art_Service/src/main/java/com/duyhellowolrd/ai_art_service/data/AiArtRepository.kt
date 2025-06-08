package com.duyhellowolrd.ai_art_service.data

import com.duyhellowolrd.ai_art_service.data.params.AiArtParams
import com.duyhellowolrd.ai_art_service.network.response.StyleData
import java.io.File

interface AiArtRepository {
    suspend fun genArtAi(params: AiArtParams): Result<File>

    suspend fun getAllStyles(): Result<StyleData>
}