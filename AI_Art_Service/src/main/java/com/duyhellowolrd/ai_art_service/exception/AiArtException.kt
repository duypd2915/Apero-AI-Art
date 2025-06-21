package com.duyhellowolrd.ai_art_service.exception

import androidx.annotation.StringRes
import com.duyhellowolrd.ai_art_service.R

class AiArtException(
    val errorReason: ErrorReason
) : RuntimeException() {
    override fun toString(): String = errorReason.name
}

enum class ErrorReason(@StringRes val resMessage: Int) {
    NetworkError(R.string.network_error),
    InternalError(R.string.internal_error),
    UnknownError(R.string.unknown_error),
    PresignedLinkError(R.string.unknown_error),
    GenerateImageError(R.string.generate_image_error)
}
