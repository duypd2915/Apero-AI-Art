package com.apero.aperoaiart.ui.screen.pickphoto

import androidx.lifecycle.viewModelScope
import com.apero.aperoaiart.base.BaseUIState
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
    private var _isDone = false
    val isDone: Boolean
        get() = _isDone

    init {
        viewModelScope.launch(Dispatchers.IO) {
            totalCount = repo.getImageCount()
            loadNextPage()
        }
    }

    fun loadNextPage() {
        val photoState = uiState.value.photoState
        if (photoState.isLoading() || PAGE_SIZE * currentPage >= totalCount) return

        viewModelScope.launch {
            try {
                val currentList =
                    (uiState.value.photoState as? BaseUIState.Success)?.data.orEmpty()
                updateState { it.copy(photoState = BaseUIState.Loading) }
                delay(1000) // Loading showing
                withContext(Dispatchers.IO) {
                    val newList = repo.loadWithPaging(currentPage, PAGE_SIZE)
                    val updatedList = currentList + newList
                    currentPage++
                    if (updatedList.size >= totalCount || newList.isEmpty()) {
                        _isDone = true
                    }
                    withContext(Dispatchers.Main) {
                        updateState {
                            it.copy(photoState = BaseUIState.Success(updatedList))
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

    fun isSelected(index: Int): Boolean {
        return (uiState.value.photoState as? BaseUIState.Success)?.data?.get(index)?.let {
            it.id == uiState.value.selectedPhoto?.id
        } ?: false
    }

    companion object {
        const val PAGE_SIZE = 63
    }
}