package com.apero.aperoaiart.ui.screen.pickphoto

import androidx.lifecycle.viewModelScope
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.base.BaseViewModel
import com.duyhellowolrd.ai_art_service.data.PhotoItem
import com.duyhellowolrd.ai_art_service.data.PhotoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PickPhotoViewModel(
    private val repo: PhotoRepository
) : BaseViewModel<PickPhotoUiState>(PickPhotoUiState()) {

    private var isLoading = false

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        if (isLoading) return
        isLoading = true

        updateState { it.copy(photoState = BaseUIState.Loading) }

        viewModelScope.launch {
            try {
                repo.loadNextPage().collect { newPage ->
                    val currentList =
                        (uiState.value.photoState as? BaseUIState.Success)?.data.orEmpty()
                    val combined = currentList + newPage

                    updateState {
                        it.copy(photoState = BaseUIState.Success(combined))
                    }
                    isLoading = false
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(photoState = BaseUIState.Error(e.message ?: "Unknown error"))
                }
                isLoading = false
            }
        }
    }

    fun resetAndReload() {
        repo.reset()
        updateState {
            it.copy(
                photoState = BaseUIState.Idle,
                selectedPhoto = null
            )
        }
        viewModelScope.launch {
            delay(100)
            loadNextPage()
        }
    }

    fun selectPhoto(photo: PhotoItem?) {
        updateState { it.copy(selectedPhoto = photo) }
    }
}