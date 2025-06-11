package com.duyhellowolrd.ai_art_service.exception

class AiArtException(
    private val errorReason: ErrorReason
) : RuntimeException() {
    override fun toString(): String = errorReason.name
}

enum class ErrorReason {
    NetworkError,
    InternalError,
    UnknownError,
    ImageTypeInvalid,
    PresignedLinkError
}
