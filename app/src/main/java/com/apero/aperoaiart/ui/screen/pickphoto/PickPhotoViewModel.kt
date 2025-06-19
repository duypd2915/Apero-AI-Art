package com.apero.aperoaiart.ui.screen.pickphoto

import androidx.lifecycle.viewModelScope
import com.apero.aperoaiart.base.BaseViewModel
import com.duyhellowolrd.ai_art_service.data.PhotoItem
import com.duyhellowolrd.ai_art_service.data.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PickPhotoViewModel(
    private val repo: PhotoRepository
) : BaseViewModel<PickPhotoUiState>(PickPhotoUiState()) {

    private var currentPage = 1
    private var totalCount = 0

    init {
        viewModelScope.launch(Dispatchers.IO) {
            totalCount = repo.getImageCount()
            loadNextPage()
        }
    }

    fun loadNextPage() {
        val state = uiState.value
        if (state.isLoading || state.photoList.size >= totalCount) return
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, isError = false) }
            try {
                delay(300)
                val newList = withContext(Dispatchers.IO) {
                    repo.loadWithPaging(currentPage, PAGE_SIZE)
                }
                val updatedList = state.photoList + newList
                currentPage++
                updateState {
                    it.copy(
                        photoList = updatedList,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isLoading = false,
                        isError = true
                    )
                }
            }
        }
    }

    fun selectPhoto(photo: PhotoItem?) {
        updateState { it.copy(selectedPhoto = photo) }
    }

    fun isSelected(index: Int): Boolean {
        return uiState.value.photoList[index].let {
            it.id == uiState.value.selectedPhoto?.id
        }
    }

    companion object {
        const val PAGE_SIZE = 63
    }
}