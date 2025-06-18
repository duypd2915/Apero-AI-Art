package com.apero.aperoaiart.ui.screen.pickphoto

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.base.BaseViewModel
import com.duyhellowolrd.ai_art_service.data.PhotoItem
import com.duyhellowolrd.ai_art_service.data.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
class PickPhotoViewModel(
    private val repo: PhotoRepository
) : BaseViewModel<PickPhotoUiState>(PickPhotoUiState()) {

    private var currentPage = 0
    private var totalCount = 0

    init {
        viewModelScope.launch {
            totalCount = repo.getImageCount()
            loadNextPage()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadNextPage() {
        val photoState = uiState.value.photoState
        if (photoState.isLoading() || PAGE_SIZE * currentPage >= totalCount) return
        updateState { it.copy(photoState = BaseUIState.Loading) }
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val newList = repo.loadWithPaging(currentPage, PAGE_SIZE)
                    val currentList =
                        (uiState.value.photoState as? BaseUIState.Success)?.data.orEmpty()
                    currentPage++
                    val updatedList = currentList + newList
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
        const val PAGE_SIZE = 102
    }
}