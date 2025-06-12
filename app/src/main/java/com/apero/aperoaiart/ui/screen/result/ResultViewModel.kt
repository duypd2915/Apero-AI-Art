package com.apero.aperoaiart.ui.screen.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.base.BaseViewModel
import com.apero.aperoaiart.navigation.ResultRoute
import com.apero.aperoaiart.utils.requireArg
import com.duyhellowolrd.ai_art_service.utils.FileUtils
import kotlinx.coroutines.launch

class ResultViewModel(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<ResultUiState>(ResultUiState()) {
    init {
        updateState {
            it.copy(
                imageUrl = savedStateHandle.requireArg(ResultRoute.KEY_FILE_URL)
            )
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
