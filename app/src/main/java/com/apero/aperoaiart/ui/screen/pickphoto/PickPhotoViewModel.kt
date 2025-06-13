package com.apero.aperoaiart.ui.screen.pickphoto

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apero.aperoaiart.base.BaseViewModel
import com.duyhellowolrd.ai_art_service.data.PhotoItem
import com.duyhellowolrd.ai_art_service.data.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PickPhotoViewModel(
    private val repo: PhotoRepository
) : BaseViewModel<PickPhotoUiState>(PickPhotoUiState()) {

    private val _selectedPhoto = MutableStateFlow<PhotoItem?>(null)
    val selectedPhoto: StateFlow<PhotoItem?> = _selectedPhoto.asStateFlow()

    val photoPagingFlow: Flow<PagingData<PhotoItem>> = Pager(
        config = PagingConfig(pageSize = 60, enablePlaceholders = false, prefetchDistance = 20),
        pagingSourceFactory = { repo.getPhotoPagingSource() }
    ).flow

//    fun loadPhotos() {
//        updateState {
//            it.copy(photoState = BaseUIState.Loading)
//        }
//        viewModelScope.launch {
//            val photoItems = repo.getAllPhotoItems()
//            updateState {
//                it.copy(
//                    photoState = BaseUIState.Success(photoItems)
//                )
//            }
//        }
//    }

    fun selectPhoto(selectedPhoto: PhotoItem?) {
        _selectedPhoto.value = selectedPhoto
    }
}