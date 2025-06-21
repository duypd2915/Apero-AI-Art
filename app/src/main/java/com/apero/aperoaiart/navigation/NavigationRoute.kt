package com.apero.aperoaiart.navigation

import kotlinx.serialization.Serializable

@Serializable
data class ResultRoute(val fileUrl: String)

@Serializable
data object PickPhotoRoute

@Serializable
data class StyleRoute(val fileUrl: String? = null)