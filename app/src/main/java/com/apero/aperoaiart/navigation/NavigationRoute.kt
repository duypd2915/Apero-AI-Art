package com.apero.aperoaiart.navigation

import kotlinx.serialization.Serializable

@Serializable
data class ResultRoute(val fileUrl: String) {
    companion object {
        const val KEY_FILE_URL = "fileUrl" // must match param name
    }
}

@Serializable
data object PickPhotoRoute

@Serializable
data object StyleRoute