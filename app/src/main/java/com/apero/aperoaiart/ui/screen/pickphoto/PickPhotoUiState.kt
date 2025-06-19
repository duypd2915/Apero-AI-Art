package com.apero.aperoaiart.ui.screen.pickphoto

import com.duyhellowolrd.ai_art_service.data.PhotoItem

data class PickPhotoUiState(
    val photoList: List<PhotoItem> = emptyList(),
    val selectedPhoto: PhotoItem? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)