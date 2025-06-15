package com.apero.aperoaiart.ui.screen.pickphoto

import com.apero.aperoaiart.base.BaseUIState
import com.duyhellowolrd.ai_art_service.data.PhotoItem

data class PickPhotoUiState(
    val photoState: BaseUIState<List<PhotoItem>> = BaseUIState.Idle,
    val selectedPhoto: PhotoItem? = null
)