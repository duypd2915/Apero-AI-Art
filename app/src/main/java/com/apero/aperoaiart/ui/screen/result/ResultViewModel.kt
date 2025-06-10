package com.apero.aperoaiart.ui.screen.result

import androidx.lifecycle.SavedStateHandle
import com.apero.aperoaiart.base.BaseViewModel
import com.duyhellowolrd.ai_art_service.data.AiArtRepository

class ResultViewModel(
    private val aiArtRepository: AiArtRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<ResultUiState>(ResultUiState()) {

}
