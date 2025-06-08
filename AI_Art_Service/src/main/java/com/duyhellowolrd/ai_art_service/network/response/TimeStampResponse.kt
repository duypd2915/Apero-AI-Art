package com.duyhellowolrd.ai_art_service.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TimeStampResponse(
    @SerializedName("data") val data: DataTimeStamp? = null
)

@Keep
data class DataTimeStamp(
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("timestring")
    val timestring: String
)
