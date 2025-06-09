package com.duyhellowolrd.ai_art_service.network.response

import com.duyhellowolrd.ai_art_service.network.consts.ServiceConstants
import com.google.gson.annotations.SerializedName

data class StyleResponse(
    @SerializedName("data") val data: StyleData,
    @SerializedName("statusCode") val statusCode: Int = ServiceConstants.CODE_SUCCESS,
    @SerializedName("success") val success: Boolean = true
)

data class StyleData(
    @SerializedName("items") val items: List<StyleItem> = emptyList(),
)

data class StyleItem(
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
    @SerializedName("thumbnailApp") val thumbnailApp: List<ThumbnailItem>,
    @SerializedName("categories") val categories: List<String>,
    @SerializedName("segmentId") val segmentId: String,
    @SerializedName("styleType") val styleType: String,
    @SerializedName("imageSize") val imageSize: String,
    @SerializedName("thumbnail") val thumbnail: ThumbnailMap,
)

data class StyleConfig(
    @SerializedName("mode") val mode: Int,
    @SerializedName("imageSize") val imageSize: String,
    @SerializedName("baseModel") val baseModel: String,
    @SerializedName("style") val style: String,
    @SerializedName("positivePrompt") val positivePrompt: String,
    @SerializedName("negativePrompt") val negativePrompt: String,
)

data class ThumbnailItem(
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("thumbnail_type") val thumbnailType: String,
    @SerializedName("_id") val id: String
)

data class ThumbnailMap(
    @SerializedName("before") val before: String,
    @SerializedName("after") val after: String,
    @SerializedName("preview_style") val previewStyle: String,
    @SerializedName("key") val key: String,
    @SerializedName("reminder_after") val reminderAfter: String,
    @SerializedName("reminder_before") val reminderBefore: String,
    @SerializedName("noti") val noti: String
)
