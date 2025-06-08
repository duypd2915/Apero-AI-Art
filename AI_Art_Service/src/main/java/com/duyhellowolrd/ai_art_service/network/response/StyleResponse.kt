package com.duyhellowolrd.ai_art_service.network.response

import com.google.gson.annotations.SerializedName

data class StyleResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: StyleData
)

data class StyleData(
    @SerializedName("items") val items: List<StyleItem> = emptyList(),
    @SerializedName("totalItems") val totalItems: Int = items.size,
    @SerializedName("page") val page: Int = 1,
    @SerializedName("limit") val limit: Int = 10,
    @SerializedName("totalPages") val totalPages: Int = 100,
    @SerializedName("paging") val paging: Boolean = true
)

data class StyleItem(
    @SerializedName("_id") val id: String,
    @SerializedName("project") val project: String,
    @SerializedName("name") val name: String,
    @SerializedName("key") val key: String,
    @SerializedName("config") val config: StyleConfig,
    @SerializedName("mode") val mode: String,
    @SerializedName("version") val version: String,
    @SerializedName("metadata") val metadata: List<String>,
    @SerializedName("priority") val priority: Long,
    @SerializedName("thumbnailApp") val thumbnailApp: List<String>,
    @SerializedName("categories") val categories: List<String>,
    @SerializedName("segmentId") val segmentId: String,
    @SerializedName("subscriptionType") val subscriptionType: String,
    @SerializedName("aiFamily") val aiFamily: String,
    @SerializedName("styleType") val styleType: String,
    @SerializedName("imageSize") val imageSize: String,
    @SerializedName("baseModel") val baseModel: String,
    @SerializedName("shouldCollectImage") val shouldCollectImage: Boolean,
    @SerializedName("__v") val versionNumber: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("pricing") val pricing: Int
)

data class StyleConfig(
    @SerializedName("mode") val mode: Int,
    @SerializedName("baseModel") val baseModel: String,
    @SerializedName("style") val style: String
)