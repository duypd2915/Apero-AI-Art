package com.duyhellowolrd.ai_art_service.data.impl

import android.content.Context
import android.util.Log
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest.Builder
import com.duyhellowolrd.ai_art_service.AiArtServiceEntry
import com.duyhellowolrd.ai_art_service.data.AiArtRepository
import com.duyhellowolrd.ai_art_service.data.NetworkRepository
import com.duyhellowolrd.ai_art_service.data.params.AiArtParams
import com.duyhellowolrd.ai_art_service.exception.AiArtException
import com.duyhellowolrd.ai_art_service.exception.ErrorReason
import com.duyhellowolrd.ai_art_service.network.consts.ServiceConstants
import com.duyhellowolrd.ai_art_service.network.request.AiArtRequest
import com.duyhellowolrd.ai_art_service.network.response.StyleData
import com.duyhellowolrd.ai_art_service.network.service.AIStyleService
import com.duyhellowolrd.ai_art_service.network.service.AiArtService
import com.duyhellowolrd.ai_art_service.utils.FileUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody

class AiArtRepositoryImpl(
    private val context: Context,
    private val aiArtService: AiArtService,
    private val styleService: AIStyleService,
    private val networkRepository: NetworkRepository
) : AiArtRepository {
    companion object {
        private const val TAG = "AiArtRepositoryImpl"
    }

    override suspend fun genArtAi(params: AiArtParams): Result<String> {
        try {
            if (!networkRepository.isConnectedNow()) {
                return Result.failure(AiArtException(ErrorReason.NetworkError))
            }
            Log.d(TAG, "genArtAi: start gen ${params.imageUri.path}")
            val fileResult = FileUtils.saveUriToCache(context, params.imageUri)
            val imageBitmapResized = FileUtils.fileToResizedBitmap(
                fileResult,
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
                val request = createMultipartBodyAiArt(params, presignedLink.path)
                Log.d(TAG, "genArtAi: start calling genAI $request")
                val response = aiArtService.genArtAi(request)
                Log.d(
                    TAG,
                    "genArtAi: response ${response.raw()} error ${response.errorBody()?.string()}"
                )
                val urlResult = response.body()?.data?.url ?: ""
                preloadImageWithCoil(context, urlResult)
                return if (response.isSuccessful && urlResult.isNotEmpty())
                    Result.success(urlResult)
                else {
                    Log.d(TAG, "genArtAi: error when genAI ${response.message()}")
                    Result.failure(AiArtException(ErrorReason.GenerateImageError))
                }
            } else {
                Log.d(TAG, "genArtAi: error when pushToServer ${pushToServer.message()}")
                return Result.failure(AiArtException(ErrorReason.GenerateImageError))
            }
        } catch (e: Exception) {
            Log.e(TAG, "genArtAi error", e)
            return Result.failure(e)
        }
    }

    override suspend fun getAllStyles(): Result<StyleData> {
        return try {
            val response = styleService.getStyles()

            if (response.isSuccessful) {
                val data = response.body()?.data
                if (data != null) {
                    Result.success(data)
                } else {
                    Log.d(TAG, "getAllStyles: response body null ${response.raw()}")
                    Result.failure(AiArtException(ErrorReason.UnknownError))
                }
            } else {
                Log.d(TAG, "getAllStyles: error when getStyles ${response.message()}")
                Result.failure(AiArtException(ErrorReason.UnknownError))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getAllStyles error", e)
            Result.failure(AiArtException(ErrorReason.UnknownError))
        }
    }

    suspend fun preloadImageWithCoil(context: Context, url: String) {
        val request = Builder(context)
            .data(url)
            .memoryCachePolicy(CachePolicy.ENABLED) // cache in memory
            .diskCachePolicy(CachePolicy.ENABLED)   // cache in disk
            .allowHardware(false) // safer for Bitmap access if needed later
            .build()

        val imageLoader = ImageLoader(context)
       imageLoader.execute(request)
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