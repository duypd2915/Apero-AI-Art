package com.duyhellowolrd.ai_art_service.data.impl

import android.content.Context
import android.util.Log
import com.duyhellowolrd.ai_art_service.AiArtServiceEntry
import com.duyhellowolrd.ai_art_service.data.AiArtRepository
import com.duyhellowolrd.ai_art_service.data.params.AiArtParams
import com.duyhellowolrd.ai_art_service.exception.AiArtException
import com.duyhellowolrd.ai_art_service.exception.ErrorReason
import com.duyhellowolrd.ai_art_service.network.consts.ServiceConstants
import com.duyhellowolrd.ai_art_service.network.request.AiArtRequest
import com.duyhellowolrd.ai_art_service.network.response.StyleData
import com.duyhellowolrd.ai_art_service.network.service.AIStyleService
import com.duyhellowolrd.ai_art_service.network.service.AiArtService
import com.duyhellowolrd.ai_art_service.network.service.TimeStampService
import com.duyhellowolrd.ai_art_service.utils.FileUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AiArtRepositoryImpl(
    private val context: Context,
    private val aiArtService: AiArtService,
    private val timeStampService: TimeStampService,
    private val styleService: AIStyleService
) : AiArtRepository {
    companion object {
        private const val TAG = "AiArtRepositoryImpl"
    }

    override suspend fun genArtAi(params: AiArtParams): Result<File> {
        try {
            Log.d(TAG, "genArtAi: start gen ${params.imageUri.path}")
            if (!FileUtils.checkImageExtension(context, params.imageUri)) {
                return Result.failure(AiArtException(ErrorReason.ImageTypeInvalid))
            }
            val imageBitmapResized = FileUtils.uriToResizedBitmap(
                context,
                params.imageUri,
                ServiceConstants.RequestConstants.MAX_IMAGE_PIXEL,
                ServiceConstants.RequestConstants.MIN_IMAGE_PIXEL
            )
            Log.d(TAG, "genArtAi: imageResized ${imageBitmapResized.config?.name}")
            val imageFile = FileUtils.saveBitmapToCache(
                context,
                imageBitmapResized,
                "image_${System.currentTimeMillis()}.jpg"
            )
            Log.d(TAG, "genArtAi: imageFile ${imageFile.absolutePath}")
//            val timestamp = timeStampService.getTimestamp()
//            AiArtServiceEntry.setTimeStamp(timestamp.body()?.data?.timestamp)
//            Log.d(TAG, "genArtAi: timestamp ${timestamp.isSuccessful}")
            val presignedLinkResponse = aiArtService.getLink().body()
            val presignedLink = presignedLinkResponse?.data
                ?: return Result.failure(
                    AiArtException(ErrorReason.PresignedLinkError)
                )
            Log.d(TAG, "genArtAi: presignedLink ${presignedLink.path}")
            AiArtServiceEntry.setTimeStamp(presignedLinkResponse.timestamp)
            val pushToServer = aiArtService.pushImageToServer(
                url = presignedLink.url,
                file = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull()),
            )
            Log.d(
                TAG,
                "genArtAi: pushToServer ${pushToServer.code()} ${pushToServer.message()} ${pushToServer.errorBody()}"
            )
            if (pushToServer.isSuccessful) {
//                withTimeout(ServiceConstants.TIME_OUT_SECONDS) {
                val request = createMultipartBodyAiArt(params, presignedLink.path)
                Log.d(TAG, "genArtAi: start calling genAI $request")
                val response = aiArtService.genArtAi(request)
                Log.d(
                    TAG,
                    "genArtAi: response ${response.raw()} error ${response.errorBody()?.string()}"
                )
//                }
            }
            return Result.success(imageFile)
        } catch (e: Exception) {
            Log.e(TAG, "genArtAi error", e)
            return Result.failure(e)
        }
    }

    override suspend fun getAllStyles(): Result<StyleData> {
        return Result.success(styleService.getStyles().body()?.data ?: StyleData())
    }

    private fun createMultipartBodyAiArt(
        params: AiArtParams,
        image: String
    ): AiArtRequest {
        return AiArtRequest(
            file = image,
            styleId = params.styleId,
            positivePrompt = params.positivePrompt,
            negativePrompt = params.negativePrompt,
        )
    }
}