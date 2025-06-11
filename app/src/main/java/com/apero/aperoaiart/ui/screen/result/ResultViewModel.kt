package com.apero.aperoaiart.ui.screen.result

import androidx.lifecycle.SavedStateHandle
import com.apero.aperoaiart.base.BaseUIState
import com.apero.aperoaiart.base.BaseViewModel
import com.apero.aperoaiart.navigation.ResultRoute
import com.apero.aperoaiart.utils.requireArg

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

    }
}
