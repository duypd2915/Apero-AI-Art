package com.apero.aperoaiart.ui.screen.pickphoto

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.base.BaseViewModel
import com.duyhellowolrd.ai_art_service.data.PhotoItem
import com.duyhellowolrd.ai_art_service.data.PhotoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PickPhotoViewModel(
    private val repo: PhotoRepository
) : BaseViewModel<PickPhotoUiState>(PickPhotoUiState()) {

    private var isLoading = false
    private var jobLoadPhoto: Job? = null

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        if (isLoading) return
        isLoading = true

        updateState { it.copy(photoState = BaseUIState.Loading) }

        jobLoadPhoto = viewModelScope.launch {
            try {
                repo.loadNextPage()
                    .collect { newPage ->
                        val currentList =
                            (uiState.value.photoState as? BaseUIState.Success)?.data.orEmpty()
                        Log.d("PickPhotoViewModel", "loadNextPage: $currentList")
                        val combined = currentList + newPage

                        updateState {
                            it.copy(photoState = BaseUIState.Success(combined))
                        }
                        isLoading = false
                    }
            } catch (e: Exception) {
                Log.e("PickPhotoViewModel", "loadNextPage: ", e)
                updateState {
                    it.copy(photoState = BaseUIState.Error(e.message ?: "Unknown error"))
                }
                isLoading = false
            }
        }
    }

    fun selectPhoto(photo: PhotoItem?) {
        updateState { it.copy(selectedPhoto = photo) }
    }
}