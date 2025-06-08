package com.duyhellowolrd.ai_art_service.data.impl

import android.util.Log
import com.duyhellowolrd.ai_art_service.AiArtServiceEntry
import com.duyhellowolrd.ai_art_service.data.AiArtRepository
import com.duyhellowolrd.ai_art_service.data.params.AiArtParams
import com.duyhellowolrd.ai_art_service.network.request.AiArtRequest
import com.duyhellowolrd.ai_art_service.network.response.StyleData
import com.duyhellowolrd.ai_art_service.network.service.AIStyleService
import com.duyhellowolrd.ai_art_service.network.service.AiArtService
import com.duyhellowolrd.ai_art_service.network.service.TimeStampService
import java.io.File

class AiArtRepositoryImpl(
    private val aiArtService: AiArtService,
    private val timeStampService: TimeStampService,
    private val styleService: AIStyleService
) : AiArtRepository {
    companion object {
        private const val TAG = "AiArtRepositoryImpl"
    }

    override suspend fun genArtAi(params: AiArtParams): Result<File> {
        try {
            val timestamp = timeStampService.getTimestamp()
            Log.d(TAG, "genArtAi: timestamp ${timestamp.raw()}")
            Log.d(TAG, "genArtAi: timestamp error ${timestamp.errorBody()?.string()}")
            AiArtServiceEntry.setTimeStamp(timestamp.body()?.data?.timestamp)
            val presignedLink = aiArtService.getLink()
            Log.d(TAG, "genArtAi: presignedLink ${presignedLink.raw()}")
            Log.d(TAG, "genArtAi: presignedLink error ${presignedLink.errorBody()?.string()}")
            val test = aiArtService.genArtAi(AiArtRequest.Default)
            Log.d(TAG, "genArtAi: test ${test.raw()}")
            Log.d(TAG, "genArtAi: test error ${test.errorBody()?.string()}")
        } catch (e: Exception) {
            Log.e(TAG, "genArtAi error", e)
        }
        return Result.failure(IllegalStateException())
    }

    override suspend fun getAllStyles(): Result<StyleData> {
        return Result.success(styleService.getStyles().body()?.data ?: StyleData())
    }
}