package com.duyhellowolrd.ai_art_service.network.request

import com.google.gson.annotations.SerializedName

data class AiArtRequest(
    @SerializedName("file")
    val file: String,

    @SerializedName("style")
    val style: String? = null,

    @SerializedName("styleId")
    val styleId: String? = null,

    @SerializedName("positivePrompt")
    val positivePrompt: String? = null,

    @SerializedName("negativePrompt")
    val negativePrompt: String? = null,

    @SerializedName("imageSize")
    val imageSize: Int? = null,

    @SerializedName("fixHeight")
    val fixHeight: Int? = null,

    @SerializedName("fixWidth")
    val fixWidth: Int? = null,

    @SerializedName("fixWidthAndHeight")
    val fixWidthAndHeight: Boolean? = null,

    @SerializedName("useControlnet")
    val useControlnet: Boolean? = null,

    @SerializedName("applyPulid")
    val applyPulid: Boolean? = null,

    @SerializedName("seed")
    val seed: Int? = null,

    @SerializedName("fastMode")
    val fastMode: Boolean? = null
) {
    companion object {
        val Default = AiArtRequest(
            file = "",
            style = null,
            styleId = null,
            positivePrompt = null,
            negativePrompt = null,
            imageSize = null,
            fixHeight = null,
            fixWidth = null,
            fixWidthAndHeight = null,
            useControlnet = null,
            applyPulid = null,
            seed = null,
            fastMode = null
        )
    }
}