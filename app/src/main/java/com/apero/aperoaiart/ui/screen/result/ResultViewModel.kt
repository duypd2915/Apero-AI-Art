package com.apero.aperoaiart.ui.screen.result

import androidx.lifecycle.viewModelScope
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.base.BaseViewModel
import com.duyhellowolrd.ai_art_service.utils.FileUtils
import kotlinx.coroutines.launch

class ResultViewModel : BaseViewModel<ResultUiState>(ResultUiState()) {

    fun setInitUrl(url: String) {
        updateState {
            it.copy(imageUrl = url)
        }
    }

    fun resetState() {
        updateState {
            it.copy(downloadState = BaseUIState.Idle)
        }
    }

    fun onDownloadClick() {
        updateState {
            it.copy(downloadState = BaseUIState.Loading)
        }
        viewModelScope.launch {
            FileUtils.saveFileToStorage(uiState.value.imageUrl)
            updateState {
                it.copy(downloadState = BaseUIState.Success(Unit))
            }
        }
    }
}
