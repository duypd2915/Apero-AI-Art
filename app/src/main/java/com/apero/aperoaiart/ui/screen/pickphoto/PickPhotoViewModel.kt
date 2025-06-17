package com.apero.aperoaiart.ui.screen.pickphoto

import androidx.lifecycle.viewModelScope
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.base.BaseViewModel
import com.duyhellowolrd.ai_art_service.data.PhotoItem
import com.duyhellowolrd.ai_art_service.data.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PickPhotoViewModel(
    private val repo: PhotoRepository
) : BaseViewModel<PickPhotoUiState>(PickPhotoUiState()) {

    init {
        loadNextPage()
    }

    private fun loadNextPage() {
        updateState { it.copy(photoState = BaseUIState.Loading) }
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repo.loadNextPage()
                        .collect { newPage ->
                            val currentList =
                                (uiState.value.photoState as? BaseUIState.Success)?.data.orEmpty()
                            withContext(Dispatchers.Main) {
                                updateState {
                                    it.copy(photoState = BaseUIState.Success(currentList + newPage))
                                }
                            }
                        }
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(photoState = BaseUIState.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }

    fun selectPhoto(photo: PhotoItem?) {
        updateState { it.copy(selectedPhoto = photo) }
    }
}