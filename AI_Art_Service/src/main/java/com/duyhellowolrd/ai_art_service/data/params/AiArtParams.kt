package com.duyhellowolrd.ai_art_service.data.params

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import kotlin.random.Random

data class AiArtParams(
    val pathImageOrigin: String,
    val styleId: String? = null,
    val style: String? = null,
    val positivePrompt: String? = null,
    val negativePrompt: String? = null,
    @IntRange(256, 2048)
    val imageSize: Int? = null,
    @FloatRange(0.0, 1.0)
    val alpha: Float? = null,
    val prompt: String? = null,
) {
    companion object {
        // TODO
        val Default = AiArtParams(
            pathImageOrigin = "",
            styleId = Random.nextInt(1, 100).toString(),
            style = "anime",
            positivePrompt = "beautiful anime girl",
            negativePrompt = "blurry, low quality",
            imageSize = 512,
            alpha = 0.5f,
            prompt = "anime girl"
        )
    }
}