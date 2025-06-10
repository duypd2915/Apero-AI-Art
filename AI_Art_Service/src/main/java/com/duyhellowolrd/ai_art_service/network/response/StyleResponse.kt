package com.duyhellowolrd.ai_art_service.network.response

import com.duyhellowolrd.ai_art_service.network.consts.ServiceConstants
import com.google.gson.annotations.SerializedName

data class StyleResponse(
    @SerializedName("data") val data: StyleData,
    @SerializedName("statusCode") val statusCode: Int = ServiceConstants.CODE_SUCCESS,
    @SerializedName("success") val success: Boolean = true
)

data class StyleData(
    @SerializedName("items") val items: List<CategoryItem> = emptyList(),
)

data class CategoryItem(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("segmentId") val segmentId: String,
    @SerializedName("styles") val styles: List<StyleDetail>
)

data class StyleDetail(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("key") val key: String,
    @SerializedName("config") val config: StyleConfig,
    @SerializedName("categories") val categories: List<String>,
    @SerializedName("segmentId") val segmentId: String,
    @SerializedName("styleType") val styleType: String,
    @SerializedName("imageSize") val imageSize: String,
)

data class StyleConfig(
    @SerializedName("mode") val mode: Int,
    @SerializedName("imageSize") val imageSize: String,
    @SerializedName("baseModel") val baseModel: String,
    @SerializedName("style") val style: String,
    @SerializedName("positivePrompt") val positivePrompt: String? = null,
    @SerializedName("negativePrompt") val negativePrompt: String? = null,
)