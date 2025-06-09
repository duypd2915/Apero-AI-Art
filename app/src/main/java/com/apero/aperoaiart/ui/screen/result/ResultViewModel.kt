package com.apero.aperoaiart.ui.screen.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.apero.aperoaiart.base.BaseViewModel
import com.duyhellowolrd.ai_art_service.data.AiArtRepository
import com.duyhellowolrd.ai_art_service.data.params.AiArtParams
import kotlinx.coroutines.launch

class ResultViewModel(
    private val aiArtRepository: AiArtRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<ResultUiState>(ResultUiState()) {
    init {
        viewModelScope.launch {
            aiArtRepository.genArtAi(AiArtParams.Default)
        }
    }
}
